import paho.mqtt.client as mqtt
from random import uniform
import time

mqtt_broker = 'localhost'
connected = False

def on_connect(client, userdata, flags, reason_code, properties):
    global connected
    connected = True
    print(f"MQTT connected with result code {reason_code}")
    
def on_connect_fail(client, userdata):
    global connected
    connected = False
    print(f"MQTT connect fail")


mqtt_client = mqtt.Client(mqtt.CallbackAPIVersion.VERSION2)
mqtt_client.on_connect = on_connect
mqtt_client.on_connect_fail = on_connect_fail
mqtt_client.username_pw_set(username="user", password="password")
mqtt_client.connect(mqtt_broker)
mqtt_client.loop_start()

while True: 
    if(connected == True):
        randTemperature = uniform(20.0, 21.0)
        randVoltage = uniform(3.4, 5.5)
        timems = float(time.time_ns() / 1000000)
        payload = f"{randTemperature},{randVoltage},{timems}"
        mqtt_client.publish("sensor", payload)
        print('MQTT: Just published ' + payload + ' to topic sensor')
    else:
        print('MQTT: Not connected')
    time.sleep(3)
    
mqtt_client.loop_stop() # heheheh
   
