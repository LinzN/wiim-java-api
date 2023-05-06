# wiim-java-api

### What is wiim-java-api
The project wiim-java-api is a implementation of the [WiiM HTTP API](https://www.wiimhome.com/pdf/HTTP%20API%20for%20WiiM%20Mini.pdf) written in java to interact with WiiM devices.

The api is compatible with the following WiiM devices [WiiMhome](https://www.wiimhome.com/) 

- WiiM Pro  [See product](https://www.wiimhome.com/WiiMPro/Overview) 
- Wiim mini [See product](https://www.wiimhome.com/WiiMMini/Overview) 

Other WiiM devices may also work if they using the same httpAPI.
### Java version

This library was built for [Java 11](https://openjdk.java.net/projects/jdk/11/).


### API Information
This api is pulling information of the current WiiMDevice dataSet every 1000 milliseconds in the background. That means that using `GET` methods in the api will reflect data from the last successful pull.

However, `SET` methods will be applied instantly

### How to install

#### Maven

```xml
<!-- https://builds.app.stem-system.de/plugin/repository/everything/de/linzn/wiim-java-api/ -->
<dependency>
    <groupId>de.linzn</groupId>
    <artifactId>wiim-java-api</artifactId>
    <version>1.0.0</version>
</dependency>
```


### Javadocs & Documentation
In progress


### Examples

  ```java
import de.linzn.wiimJavaApi.WiimAPI;

public class ExampleDeviceStatus {
    /* IP Address of the device */
    static String wiimDeviceIPAddress = "10.50.0.99";

    public static void main(String[] args) throws InterruptedException {
        /* Creating a new wiimAPI instance with the ip address of the device */
        WiimAPI wiimAPI = new WiimAPI(wiimDeviceIPAddress);
        /* Disable ssl check because no valid certificate is given from the wiiMDevice by default */
        wiimAPI.setSslCheck(false);
        /* Connect to the httpAPI from the WiiMDevice */
        /* The current dataSet will be pulled automatic every 1000 ms*/
        wiimAPI.connect();

        /* Infinity loop to check frequently device status and the name of the device */
        while (true) {
            /* Get the current status of the player. Like "stop, play, ..."*/
            String name = wiimAPI.getDeviceInformation().get_DeviceName();
            String currentStatus = wiimAPI.getWiimPlayer().get_status();
            System.out.println("Current status of the device " + name + " is " + currentStatus);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
  ```

If you need some more examples to get started, check
the [examples' directory](https://github.com/LinzN/wiim-java-api/tree/master/src/main/java/examples) in this project.
Any contribution is welcomed!
