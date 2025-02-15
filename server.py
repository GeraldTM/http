import socket, json, os

class TCPServer:
    def __init__(self, host='127.0.0.1', port=8888 ):
        self.host = host
        self.port = port
    
    def start(self):
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        s.bind((self.host,self.port))
        s.listen(5)
        
        print('listening at ', s.getsockname())
        
        while True:
            conn, addr = s.accept()
            print('connected by ', addr)
            data = conn.recv(1024)
            
            response =  self.handle_request(data)
            
            conn.sendall(response)
            conn.close()
    
    def handle_request(self, data):
        return data
    

        

class HTTPServer(TCPServer):
    
    status_codes = json.load(open('./http_status_codes.json'))
    
    headers = {
        'Server': 'Custom GTM',
        'Content-Type': 'text/html'
    }
    
    def handle_request(self, data):
        print(data)

        request = HTTPRequest(data)

        try:
            handler = getattr(self, 'handle_%s' % request.method)
        except AttributeError:
            handler = self.HTTP_501_handler
        response = handler(request)
        print(response)
        return response

    def HTTP_501_handler(self, request):
        response_line = self.response_line(status_code=501)
        response_headers = self.response_headers()
        blank_line = b"\r\n"
        response_body = b"<h1> 501 Not Implemented </h1> <br> <p> This site has not implemented a response to "+request.method.encode()+b" requests </p>"

        return b"".join([response_line, response_headers, blank_line, response_body])

    def handle_GET(self, request):
        filename = request.uri.strip('/')

        if os.path.exists(filename):
            response_line = self.response_line(status_code=200)

            response_headers = self.response_headers()

            with open(filename, 'rb') as f:
                response_body = f.read()
        else:
            response_line = self.response_line(status_code=404)
            response_headers = self.response_headers()
            response_body = b"<h1>404 Not Found <br> <p> file "+filename.encode()+ b" not found </p>"
        blank_line = b"\r\n"
        return b"".join([response_line, response_headers, blank_line, response_body])


    def response_line(self, status_code):
        reason = self.status_codes['%s' % status_code]
        line = 'HTTP/1.1 %s %s \r\n' % (status_code, reason['message'])
        return line.encode()
    
    def response_headers(self, extra_headers=None):
        headers_copy = self.headers.copy()
        
        if extra_headers:
            headers_copy.update(extra_headers)
        
        headers = ""
        
        for header in headers_copy:
            headers += '%s: %s\r\n' % (header, headers_copy[header])
        
        return headers.encode()

class HTTPRequest:
    def __init__(self, data):
        self.method = None
        self.uri = None
        self.http_version = "1.1"

        self.parse(data)

    def parse(self, data):
        lines = data.split(b"\r\n")
        request_line = lines[0]

        words = request_line.split(b" ")
        self.method = words[0].decode()

        if len(words) > 1:
            self.uri = words[1].decode()

        if len(words) > 2:
            self.http_version = words[2]


if __name__ == '__main__':
    server = HTTPServer()
    server.start()

