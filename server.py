import socket, json

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
        response_line = self.response_line(200)
        headers = self.response_headers()
        blank_line = b"\r\n"
        response_body = open("./test.html", "rb").read()
        return b"".join([response_line, blank_line, response_body])
    
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

if __name__ == '__main__':
    server = HTTPServer()
    server.start()

