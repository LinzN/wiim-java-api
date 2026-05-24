/*
 * Copyright (c) 2026 MirraNET, Niklas Linz. All rights reserved.
 *
 * This file is part of the MirraNET project and is licensed under the
 * GNU Lesser General Public License v3.0 (LGPLv3).
 *
 * You may use, distribute and modify this code under the terms
 * of the LGPLv3 license. You should have received a copy of the
 * license along with this file. If not, see <https://www.gnu.org/licenses/lgpl-3.0.html>
 * or contact: niklas.linz@mirranet.de
 */

package de.linzn.wiim.api.exception;

/**
 * Exception thrown when a WiiM API call fails.
 */
public class WiiMApiException extends RuntimeException {

    private final int statusCode;

    public WiiMApiException(String message) {
        super(message);
        this.statusCode = -1;
    }

    public WiiMApiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public WiiMApiException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = -1;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
