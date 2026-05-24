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

package de.linzn.wiim.api;

import java.util.logging.Level;
import java.util.logging.Logger;

public class WiiMDefaultLogger implements IWiiMLogger{

    private static final Logger LOG = Logger.getLogger(WiiMDefaultLogger.class.getName());

    @Override
    public void error(Object input, Exception e) {
        if(e != null) {
            LOG.log(Level.SEVERE, input.toString(), e);
        } else {
            LOG.log(Level.SEVERE, input.toString());
        }
    }

    @Override
    public void warning(Object input, Exception e) {
        if(e != null) {
            LOG.log(Level.WARNING, input.toString(), e);
        } else {
            LOG.log(Level.WARNING, input.toString());
        }
    }

    @Override
    public void info(Object input) {
        LOG.log(Level.INFO, input.toString());
    }

    @Override
    public void debug(Object input) {
        LOG.log(Level.FINEST, input.toString());
    }
}
