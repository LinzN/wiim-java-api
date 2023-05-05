/*
 * Copyright (C) 2019. Niklas Linz - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the LGPLv3 license, which unfortunately won't be
 * written for another century.
 *
 * You should have received a copy of the LGPLv3 license with
 * this file. If not, please write to: niklas.linz@enigmar.de
 *
 */

package de.linzn.wiimJavaApi;

import de.linzn.wiimJavaApi.response.TelegramResponse;

public class WiimAPI {
    private String token;

    public WiimAPI(String token) {
        this.token = token;
    }

    public TelegramResponse<String> sendMessage(String chatID, String text) {
        return SendMessage.init(this.token, chatID, text).sendAPIRequest();
    }

    public TelegramResponse<String> getUpdate(long offset) {
        return GetUpdate.init(this.token, offset).sendAPIRequest();
    }
}
