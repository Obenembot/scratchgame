

## Start Up
```angular2html
docker compose up
```

### Set up a GELF UDP Input to push input
```angular2html
Create a GELF UDP Input
Before logs show up, Graylog needs to listen on UDP port 12201:

Go to "System" > "Inputs"

From the dropdown, select GELF UDP

Click "Launch new input"

In the dialog:

Node: select the node shown

Port: keep default 12201

Bind address: leave as 0.0.0.0

Title (optional): e.g., Spring Boot GELF

Click "Launch"

```