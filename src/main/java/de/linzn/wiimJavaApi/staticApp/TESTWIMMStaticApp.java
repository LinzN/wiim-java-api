/*
 * Copyright (C) 2023. Niklas Linz - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the LGPLv3 license, which unfortunately won't be
 * written for another century.
 *
 * You should have received a copy of the LGPLv3 license with
 * this file. If not, please write to: niklas.linz@enigmar.de
 *
 */

package de.linzn.wiimJavaApi.staticApp;

import de.linzn.wiimJavaApi.WiimAPI;

public class TESTWIMMStaticApp {

    public static void main(String[] args) throws InterruptedException {
        WiimAPI wiimAPI = new WiimAPI("10.50.0.99");
        wiimAPI.setSslCheck(false);
        wiimAPI.connect();
        while (true) {
            System.out.println("PlayerStatus: " + wiimAPI.getPlayerStatus().get_status());
            Thread.sleep(900);
        }
    }
}
