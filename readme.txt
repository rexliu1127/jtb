1. "mvn validate" 安装本地jar

2. 使用nginx转发需要set header
 location /m {
            index  index.html index.htm;
            proxy_set_header        Host $host;
            proxy_set_header        X-Real-IP $remote_addr;
            proxy_set_header        REMOTE-HOST $remote_addr;
            proxy_set_header        X-Forwarded-For $proxy_add_x_forwarded_for;
            #proxy_set_header        X-Forwarded-Proto https; ##当nginx使用https时需要
            proxy_pass http://localhost:8081/m;
            proxy_read_timeout  90;
        } 
