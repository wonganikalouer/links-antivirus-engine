@echo off
netsh wlan set hostednetwork ssid=HOTSPOT key=12345678 mode=allow keyUsage=persistence

netsh wlan start hostednetwork
pause