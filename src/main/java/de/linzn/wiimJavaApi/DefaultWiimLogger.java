/*
 * Copyright (c) 2025 MirraNET, Niklas Linz. All rights reserved.
 *
 * This file is part of the MirraNET project and is licensed under the
 * GNU Lesser General Public License v3.0 (LGPLv3).
 *
 * You may use, distribute and modify this code under the terms
 * of the LGPLv3 license. You should have received a copy of the
 * license along with this file. If not, see <https://www.gnu.org/licenses/lgpl-3.0.html>
 * or contact: niklas.linz@mirranet.de
 */

package de.linzn.wiimJavaApi;

class DefaultWiimLogger implements IWiimLogger{
    @Override
    public void error(Object input) {
        System.err.println(input);
    }

    @Override
    public void warning(Object input) {
        System.out.println(input);
    }

    @Override
    public void info(Object input) {
        System.out.println(input);
    }

    @Override
    public void debug(Object input) {
        System.out.println(input);
    }
}
