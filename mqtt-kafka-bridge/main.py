import paho.mqtt.client as mqtt
from pykafka import KafkaClient
import json
import datetime

# MQTT 
mqtt_broker = 'localhost'
port = 1883

def on_connect(client, userdata, flags, reason_code, properties):
    print(f"Connected with result code {reason_code}")
    client.subscribe('sensor')

def on_message(client, userdata, message):
    msg_payload = str(message.payload,'utf-8')
    print('Recieved MQTT message ', msg_payload)
    msgArr = msg_payload.split(",")
    temperature = msgArr[0]
    voltage = msgArr[1]
    time = msgArr[2]
    print("time: " + time)
    send_to_kafka(temperature, voltage, time)
    
def on_connect_fail(client, userdata):
    print(f"MQTT connect fail")
    
mqtt_client = mqtt.Client(mqtt.CallbackAPIVersion.VERSION2)
mqtt_client.on_connect = on_connect
mqtt_client.on_message = on_message
mqtt_client.on_connect_fail = on_connect_fail
mqtt_client.username_pw_set(username="user", password="password")
mqtt_client.connect(mqtt_broker)

# KAFKA
kafka_client = KafkaClient(hosts='localhost:9092')
kafka_topic = kafka_client.topics['incoming-temperatures']
kafka_producer = kafka_topic.get_sync_producer()

kafka_topic_voltage = kafka_client.topics['incoming-voltage']
kafka_producer_voltage = kafka_topic_voltage.get_sync_producer()

def send_to_kafka(temperature, voltage, time):
    time = datetime.datetime.fromtimestamp(float(time)/1000.0)
    time = time.strftime("%Y-%m-%d %H:%M:%S")
    tempjson = json.dumps({"sensordate": time, "temperature": temperature})
    kafka_producer.produce(str(tempjson).encode('ascii'))
    print('KAFKA: Just published ' + str(tempjson) + ' to topic incoming-temperatures')
    voltjson = json.dumps({"sensordate": time, "voltage": voltage})
    kafka_producer_voltage.produce(str(voltjson).encode('ascii'))
    print('KAFKA: Just published ' + str(voltjson) + ' to topic incoming-voltage')


mqtt_client.loop_forever()
