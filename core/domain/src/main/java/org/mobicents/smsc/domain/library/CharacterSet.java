/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2015, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.smsc.domain.library;

/**
 *
 * @author sergey vetyutnev
 *
 */
public enum CharacterSet {

    GSM7(0), GSM8(1), UCS2(2), Reserved(3);

    private int code;

    private CharacterSet(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static CharacterSet getInstance(int code) {
        switch (code) {
            case 0:
                return GSM7;
            case 1:
                return GSM8;
            case 2:
                return UCS2;
            default:
                return Reserved;
        }
    }
}
