package vn.com.dne.dnesdk.model;

public class Device {
    public String deviceName;
	public String deviceAddress;

    public Device(String deviceName, String deviceAddress) {
        this.deviceName = deviceName;
        this.deviceAddress = deviceAddress;
    }

    @Override
    public String toString() {
        return "Device{" +
                "deviceName='" + deviceName + '\'' +
                ", deviceAddress='" + deviceAddress + '\'' +
                '}';
    }
}