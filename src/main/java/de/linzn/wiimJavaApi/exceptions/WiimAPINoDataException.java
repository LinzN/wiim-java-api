/*
 * Copyright (C) 2023. Niklas Linz - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the given license in the LICENSE file
 */
package de.linzn.wiimJavaApi.exceptions;

public class WiimAPINoDataException extends IllegalArgumentException {
    public WiimAPINoDataException() {
        super("No data in this field yet!");
    }
}
