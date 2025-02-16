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
        with open("log.txt", "a") as log:
            log.write("Request or response not ok: " + response_line.decode() + " | request as follows: " + request.raw.decode())
        return b"".join([response_line, response_headers, blank_line, response_body])

    def handle_GET(self, request):
        filename = request.uri.strip('/')

        if filename == "":
            filename = "index.html"

        if os.path.exists(filename):
            response_line = self.response_line(status_code=200)

            response_headers = self.response_headers()

            with open(filename, 'rb') as f:
                response_body = f.read()
                f.close()
        else:
            response_line = self.response_line(status_code=404)
            response_headers = self.response_headers()
            response_body = b"<h1>404 Not Found <br> <p> file "+filename.encode()+ b" not found </p>"
            with open("log.txt", "a") as log:
                log.write("Request or response not ok: " + response_line.decode() + " | request as follows: " + request.raw.decode())
        blank_line = b"\r\n"
        return b"".join([response_line, response_headers, blank_line, response_body])

    def handle_POST(self, request):
        if request.body =='':
            response_line = self.response_line(status_code=400)
            with open("log.txt", "a") as log:
                log.write("Request or response not ok: " + response_line.decode() + " | request as follows: " + request.raw.decode())
        else:
            try:
                with open("messages.txt", "a") as f:
                    f.write(request.body.decode())
                    f.close()
                    response_line = self.response_line(status_code=200)
            except Exception as e:
                print(e)
                response_line = self.response_line(status_code=500)
                with open("log.txt", "a") as log:
                    log.write("Request or response not ok: " + response_line.decode() + " | request as follows from " + request.origin.decode() + ": " + request + "| server encountered the following issue fulfilling this request: " + repr(e))


        response_headers = self.response_headers()
        blank_line = b"\r\n"
        return b"".join([response_line,response_headers,blank_line])



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
        self.raw = data
        self.method = None
        self.uri = None
        self.http_version = "1.1"
        self.body = None
        self.parse(data)
        self.origin = None

    def parse(self, data):
        self.body = data.split(b"\r\n\r\n")[1]
        lines = data.split(b"\r\n")
        request_line = lines[0]

        words = request_line.split(b" ")
        self.method = words[0].decode()

        if len(words) > 1:
            self.uri = words[1].decode()

        if len(words) > 2:
            self.http_version = words[2]

        try:
            self.origin = list(filter(lambda x: b"Origin" in x, lines))
            self.origin = self.origin[0].split(b":")[1]
        except ValueError and IndexError:
            pass





if __name__ == '__main__':
    server = HTTPServer()
    server.start()