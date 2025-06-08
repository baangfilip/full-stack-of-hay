import paho.mqtt.client as mqtt # type: ignore
from pykafka import KafkaClient # type: ignore
import json
from datetime import datetime 
import argparse
import configparser
import os.path

def on_connect(client, userdata, flags, reason_code, properties):
    print(f"Connected with result code {reason_code}")
    client.subscribe('sensor')

def on_message(client, userdata, message):
    msg_payload: str = str(message.payload,'utf-8')
    print('Recieved MQTT message ', msg_payload)
    msgArr: list[str] = msg_payload.split(",")
    temperature: float = float(msgArr[0])
    voltage: float = float(msgArr[1])
    time: float = float(msgArr[2])
    friendlyName: str = msgArr[3]
    sensorid: str = msgArr[4]
    print("time: " + time)
    send_to_kafka(temperature, voltage, time, friendlyName, sensorid)
    
def on_connect_fail(client, userdata):
    print(f"MQTT connect fail")

def send_to_kafka(temperature: float, voltage: float, time: float, friendlyName: str, sensorid: str):
    timeobj: datetime = datetime.fromtimestamp(time/1000.0)
    datetimestr: str = timeobj.strftime("%Y-%m-%d %H:%M:%S")
    tempjson: str = json.dumps({"sensordate": datetimestr, "temperature": temperature, "friendlyname": friendlyName, "sensorid": sensorid})
    kafka_producer.produce(str(tempjson).encode('ascii'))
    print('KAFKA: Just published ' + str(tempjson) + ' to topic incoming-temperatures')
    voltjson: str = json.dumps({"sensordate": datetimestr, "voltage": voltage, "friendlyname": friendlyName, "sensorid": sensorid})
    kafka_producer_voltage.produce(str(voltjson).encode('ascii'))
    print('KAFKA: Just published ' + str(voltjson) + ' to topic incoming-voltage')

print("start")

try: 
    parser = argparse.ArgumentParser()
    parser.add_argument("--config", help="define the path to the config file", default="./config")
    args = parser.parse_args()
    if os.path.isfile(args.config) == False:
        raise Exception(f"Can not find config at path {args.config}") 
    config = configparser.ConfigParser()
    config.read(args.config)
    print(config.sections())
    
    # MQTT 
    mqtt_broker = config.get('MQTT', 'host', fallback='localhost')
    port = config.get('MQTT', 'host', fallback=1883)
    username = config.get('MQTT', 'user', fallback='')
    password = config.get('MQTT', 'password', fallback='')
    if bool(username) ^ bool(password):
        raise Exception(f"MQTT config error: username and password must be given together") 
    print(password)
    mqtt_client = mqtt.Client(mqtt.CallbackAPIVersion.VERSION2)
    mqtt_client.on_connect = on_connect
    mqtt_client.on_message = on_message
    mqtt_client.on_connect_fail = on_connect_fail
    mqtt_client.username_pw_set(username=username, password=password)
    mqtt_client.connect(mqtt_broker)

    # KAFKA
    kafkahost = f"{config.get('KAFKA', 'host', fallback='localhost')}:{config.get('KAFKA', 'port', fallback=9092)}"
    temptopic = password = config.get('KAFKA', 'temptopic', fallback='incoming-temperatures-fallback')
    
    kafka_client = KafkaClient(hosts=kafkahost)
    kafka_topic = kafka_client.topics[temptopic]
    kafka_producer = kafka_topic.get_sync_producer()

    volttopic = password = config.get('KAFKA', 'voltagetopic', fallback='incoming-voltage-fallback')
    kafka_topic_voltage = kafka_client.topics[volttopic]
    kafka_producer_voltage = kafka_topic_voltage.get_sync_producer()

    mqtt_client.loop_forever()
finally:
    print("finally")



