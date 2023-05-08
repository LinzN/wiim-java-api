/*
 * Copyright (C) 2023. Niklas Linz - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the given license in the LICENSE file
 */
package de.linzn.wiimJavaApi;


import de.linzn.wiimJavaApi.exceptions.WiimAPINoDataException;
import org.json.JSONObject;

import java.util.Date;

public class DeviceInformation extends HttpAPIAccess {
    DeviceInformation(WiimAPI wiimAPI) {
        super("getStatusEx", wiimAPI);
    }

    public String get_language() {
        if (this.dataSet.has("language")) {
            return this.dataSet.getString("language");
        }
        throw new WiimAPINoDataException();
    }

    public String get_ssid() {
        if (this.dataSet.has("ssid")) {
            return this.dataSet.getString("ssid");
        }
        throw new WiimAPINoDataException();
    }

    public String get_hideSSID() {
        if (this.dataSet.has("hideSSID")) {
            return this.dataSet.getString("hideSSID");
        }
        throw new WiimAPINoDataException();
    }

    public String get_firmware() {
        if (this.dataSet.has("firmware")) {
            return this.dataSet.getString("firmware");
        }
        throw new WiimAPINoDataException();
    }

    public String get_build() {
        if (this.dataSet.has("build")) {
            return this.dataSet.getString("build");
        }
        throw new WiimAPINoDataException();
    }

    public String get_project() {
        if (this.dataSet.has("project")) {
            return this.dataSet.getString("project");
        }
        throw new WiimAPINoDataException();
    }

    public String get_priv_prj() {
        if (this.dataSet.has("priv_prj")) {
            return this.dataSet.getString("priv_prj");
        }
        throw new WiimAPINoDataException();
    }

    public long get_Release() {
        if (this.dataSet.has("Release")) {
            return this.dataSet.getLong("Release");
        }
        throw new WiimAPINoDataException();
    }

    public String get_FW_Release_version() {
        if (this.dataSet.has("FW_Release_version")) {
            return this.dataSet.getString("FW_Release_version");
        }
        throw new WiimAPINoDataException();
    }

    public int get_group() {
        if (this.dataSet.has("group")) {
            return this.dataSet.getInt("group");
        }
        throw new WiimAPINoDataException();
    }

    public double get_wmrm_version() {
        if (this.dataSet.has("wmrm_version")) {
            return this.dataSet.getDouble("wmrm_version");
        }
        throw new WiimAPINoDataException();
    }

    public int get_expired() {
        if (this.dataSet.has("expired")) {
            return this.dataSet.getInt("expired");
        }
        throw new WiimAPINoDataException();
    }

    public int get_internet() {
        if (this.dataSet.has("internet")) {
            return this.dataSet.getInt("internet");
        }
        throw new WiimAPINoDataException();
    }

    public String get_uuid() {
        if (this.dataSet.has("uuid")) {
            return this.dataSet.getString("uuid");
        }
        throw new WiimAPINoDataException();
    }

    public String get_MAC() {
        if (this.dataSet.has("MAC")) {
            return this.dataSet.getString("MAC");
        }
        throw new WiimAPINoDataException();
    }

    public String get_BT_MAC() {
        if (this.dataSet.has("BT_MAC")) {
            return this.dataSet.getString("BT_MAC");
        }
        throw new WiimAPINoDataException();
    }

    public String get_AP_MAC() {
        if (this.dataSet.has("AP_MAC")) {
            return this.dataSet.getString("AP_MAC");
        }
        throw new WiimAPINoDataException();
    }

    public String get_date() {
        if (this.dataSet.has("date")) {
            return this.dataSet.getString("date");
        }
        throw new WiimAPINoDataException();
    }

    public String get_time() {
        if (this.dataSet.has("time")) {
            return this.dataSet.getString("time");
        }
        throw new WiimAPINoDataException();
    }

    public int get_netstat() {
        if (this.dataSet.has("netstat")) {
            return this.dataSet.getInt("netstat");
        }
        throw new WiimAPINoDataException();
    }

    public String get_essid() {
        if (this.dataSet.has("essid")) {
            return this.dataSet.getString("essid");
        }
        throw new WiimAPINoDataException();
    }

    public String get_apcli0() {
        if (this.dataSet.has("apcli0")) {
            return this.dataSet.getString("apcli0");
        }
        throw new WiimAPINoDataException();
    }

    public String get_eth0() {
        if (this.dataSet.has("eth0")) {
            return this.dataSet.getString("eth0");
        }
        throw new WiimAPINoDataException();
    }

    public String get_ETH_MAC() {
        if (this.dataSet.has("ETH_MAC")) {
            return this.dataSet.getString("ETH_MAC");
        }
        throw new WiimAPINoDataException();
    }

    public String get_hardware() {
        if (this.dataSet.has("hardware")) {
            return this.dataSet.getString("hardware");
        }
        throw new WiimAPINoDataException();
    }

    public int get_VersionUpdate() {
        if (this.dataSet.has("VersionUpdate")) {
            return this.dataSet.getInt("VersionUpdate");
        }
        throw new WiimAPINoDataException();
    }

    public int get_NewVer() {
        if (this.dataSet.has("NewVer")) {
            return this.dataSet.getInt("NewVer");
        }
        throw new WiimAPINoDataException();
    }

    public int get_mcu_ver() {
        if (this.dataSet.has("mcu_ver")) {
            return this.dataSet.getInt("mcu_ver");
        }
        throw new WiimAPINoDataException();
    }

    public int get_mcu_ver_new() {
        if (this.dataSet.has("mcu_ver_new")) {
            return this.dataSet.getInt("mcu_ver_new");
        }
        throw new WiimAPINoDataException();
    }

    public int get_update_check_count() {
        if (this.dataSet.has("update_check_count")) {
            return this.dataSet.getInt("update_check_count");
        }
        throw new WiimAPINoDataException();
    }

    public String get_ra0() {
        if (this.dataSet.has("ra0")) {
            return this.dataSet.getString("ra0");
        }
        throw new WiimAPINoDataException();
    }

    public String get_temp_uuid() {
        if (this.dataSet.has("temp_uuid")) {
            return this.dataSet.getString("temp_uuid");
        }
        throw new WiimAPINoDataException();
    }

    public String get_cap1() {
        if (this.dataSet.has("cap1")) {
            return this.dataSet.getString("cap1");
        }
        throw new WiimAPINoDataException();
    }

    public String get_capability() {
        if (this.dataSet.has("capability")) {
            return this.dataSet.getString("capability");
        }
        throw new WiimAPINoDataException();
    }

    public String get_languages() {
        if (this.dataSet.has("languages")) {
            return this.dataSet.getString("languages");
        }
        throw new WiimAPINoDataException();
    }

    public int get_prompt_status() {
        if (this.dataSet.has("prompt_status")) {
            return this.dataSet.getInt("prompt_status");
        }
        throw new WiimAPINoDataException();
    }

    public long get_alexa_ver() {
        if (this.dataSet.has("alexa_ver")) {
            return this.dataSet.getLong("alexa_ver");
        }
        throw new WiimAPINoDataException();
    }

    public int get_alexa_beta_enable() {
        if (this.dataSet.has("alexa_beta_enable")) {
            return this.dataSet.getInt("alexa_beta_enable");
        }
        throw new WiimAPINoDataException();
    }

    public int get_alexa_force_beta_cfg() {
        if (this.dataSet.has("alexa_force_beta_cfg")) {
            return this.dataSet.getInt("alexa_force_beta_cfg");
        }
        throw new WiimAPINoDataException();
    }

    public int get_dsp_ver() {
        if (this.dataSet.has("dsp_ver")) {
            return this.dataSet.getInt("dsp_ver");
        }
        throw new WiimAPINoDataException();
    }

    public String get_streams_all() {
        if (this.dataSet.has("streams_all")) {
            return this.dataSet.getString("streams_all");
        }
        throw new WiimAPINoDataException();
    }

    public String get_streams() {
        if (this.dataSet.has("streams")) {
            return this.dataSet.getString("streams");
        }
        throw new WiimAPINoDataException();
    }

    public String get_region() {
        if (this.dataSet.has("region")) {
            return this.dataSet.getString("region");
        }
        throw new WiimAPINoDataException();
    }

    public int get_volume_control() {
        if (this.dataSet.has("volume_control")) {
            return this.dataSet.getInt("volume_control");
        }
        throw new WiimAPINoDataException();
    }

    public String get_external() {
        if (this.dataSet.has("external")) {
            return this.dataSet.getString("external");
        }
        throw new WiimAPINoDataException();
    }

    public int get_preset_key() {
        if (this.dataSet.has("preset_key")) {
            return this.dataSet.getInt("preset_key");
        }
        throw new WiimAPINoDataException();
    }

    public String get_plm_support() {
        if (this.dataSet.has("plm_support")) {
            return this.dataSet.getString("plm_support");
        }
        throw new WiimAPINoDataException();
    }

    public int get_lbc_support() {
        if (this.dataSet.has("lbc_support")) {
            return this.dataSet.getInt("lbc_support");
        }
        throw new WiimAPINoDataException();
    }

    public String get_WifiChannel() {
        if (this.dataSet.has("WifiChannel")) {
            return this.dataSet.getString("WifiChannel");
        }
        throw new WiimAPINoDataException();
    }

    public String get_RSSI() {
        if (this.dataSet.has("RSSI")) {
            return this.dataSet.getString("RSSI");
        }
        throw new WiimAPINoDataException();
    }

    public String get_BSSID() {
        if (this.dataSet.has("type")) {
            return this.dataSet.getString("BSSID");
        }
        throw new WiimAPINoDataException();
    }

    public int get_wlanFreq() {
        if (this.dataSet.has("wlanFreq")) {
            return this.dataSet.getInt("wlanFreq");
        }
        throw new WiimAPINoDataException();
    }

    public int get_wlanDataRate() {
        if (this.dataSet.has("wlanDataRate")) {
            return this.dataSet.getInt("wlanDataRate");
        }
        throw new WiimAPINoDataException();
    }

    public int get_battery() {
        if (this.dataSet.has("battery")) {
            return this.dataSet.getInt("battery");
        }
        throw new WiimAPINoDataException();
    }

    public int get_battery_percent() {
        if (this.dataSet.has("battery_percent")) {
            return this.dataSet.getInt("battery_percent");
        }
        throw new WiimAPINoDataException();
    }

    public int get_securemode() {
        if (this.dataSet.has("securemode")) {
            return this.dataSet.getInt("securemode");
        }
        throw new WiimAPINoDataException();
    }

    public String get_ota_interface_ver() {
        if (this.dataSet.has("ota_interface_ver")) {
            return this.dataSet.getString("ota_interface_ver");
        }
        throw new WiimAPINoDataException();
    }

    public String get_upnp_version() {
        if (this.dataSet.has("upnp_version")) {
            return this.dataSet.getString("upnp_version");
        }
        throw new WiimAPINoDataException();
    }

    public String get_upnp_uuid() {
        if (this.dataSet.has("upnp_uuid")) {
            return this.dataSet.getString("upnp_uuid");
        }
        throw new WiimAPINoDataException();
    }

    public int get_uart_pass_port() {
        if (this.dataSet.has("uart_pass_port")) {
            return this.dataSet.getInt("uart_pass_port");
        }
        throw new WiimAPINoDataException();
    }

    public int get_communication_port() {
        if (this.dataSet.has("communication_port")) {
            return this.dataSet.getInt("communication_port");
        }
        throw new WiimAPINoDataException();
    }

    public int get_web_firmware_update_hide() {
        if (this.dataSet.has("web_firmware_update_hide")) {
            return this.dataSet.getInt("web_firmware_update_hide");
        }
        throw new WiimAPINoDataException();
    }

    public String get_tidal_version() {
        if (this.dataSet.has("tidal_version")) {
            return this.dataSet.getString("tidal_version");
        }
        throw new WiimAPINoDataException();
    }

    public String get_service_version() {
        if (this.dataSet.has("service_version")) {
            return this.dataSet.getString("service_version");
        }
        throw new WiimAPINoDataException();
    }

    public String get_EQ_support() {
        if (this.dataSet.has("EQ_support")) {
            return this.dataSet.getString("EQ_support");
        }
        throw new WiimAPINoDataException();
    }

    public String get_HiFiSRC_version() {
        if (this.dataSet.has("HiFiSRC_version")) {
            return this.dataSet.getString("HiFiSRC_version");
        }
        throw new WiimAPINoDataException();
    }

    public String get_power_mode() {
        if (this.dataSet.has("power_mode")) {
            return this.dataSet.getString("power_mode");
        }
        throw new WiimAPINoDataException();
    }

    public String get_security() {
        if (this.dataSet.has("security")) {
            return this.dataSet.getString("security");
        }
        throw new WiimAPINoDataException();
    }

    public String get_security_version() {
        if (this.dataSet.has("security_version")) {
            return this.dataSet.getString("security_version");
        }
        throw new WiimAPINoDataException();
    }

    public String get_security_capabilities_ver() {
        if (this.dataSet.has("security_version") && this.dataSet.getJSONObject("security_version").has("ver")) {
            return this.dataSet.getJSONObject("security_version").getString("ver");
        }
        throw new WiimAPINoDataException();
    }

    public String get_security_capabilities_aes_ver() {
        if (this.dataSet.has("security_version") && this.dataSet.getJSONObject("security_version").has("aes_ver")) {
            return this.dataSet.getJSONObject("security_version").getString("aes_ver");
        }
        throw new WiimAPINoDataException();
    }

    public String get_public_https_version() {
        if (this.dataSet.has("public_https_version")) {
            return this.dataSet.getString("public_https_version");
        }
        throw new WiimAPINoDataException();
    }

    public int get_privacy_mode() {
        if (this.dataSet.has("privacy_mode")) {
            return this.dataSet.getInt("privacy_mode");
        }
        throw new WiimAPINoDataException();
    }

    public String get_DeviceName() {
        if (this.dataSet.has("DeviceName")) {
            return this.dataSet.getString("DeviceName");
        }
        throw new WiimAPINoDataException();
    }

    public String get_GroupName() {
        if (this.dataSet.has("GroupName")) {
            return this.dataSet.getString("GroupName");
        }
        throw new WiimAPINoDataException();
    }

    @Override
    void processNewData(JSONObject jsonObject) {
        this.dataSet = jsonObject;
        this.lastPull = new Date();
    }
}
