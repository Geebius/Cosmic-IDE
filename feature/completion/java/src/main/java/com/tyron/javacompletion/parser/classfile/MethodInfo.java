/*
 * This file is part of Cosmic IDE.
 * Cosmic IDE is a free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Cosmic IDE is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Cosmic IDE. If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * This file is part of Cosmic IDE.
 * Cosmic IDE is a free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * Cosmic IDE is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with Cosmic IDE. If not, see <https://www.gnu.org/licenses/>.
 */

/*
 *  This file is part of CodeAssist.
 *
 *  CodeAssist is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CodeAssist is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with CodeAssist.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.tyron.javacompletion.parser.classfile;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import java.util.EnumSet;

/**
 * method_info structure in a .class file.
 *
 * <p>See <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.6">...</a>
 */
@AutoValue
public abstract class MethodInfo {
    public static MethodInfo create(
            EnumSet<AccessFlag> accessFlags,
            int nameIndex,
            int descriptorIndex,
            ImmutableList<AttributeInfo> attributeInfo) {
        return new AutoValue_MethodInfo(accessFlags, nameIndex, descriptorIndex, attributeInfo);
    }

    public abstract EnumSet<AccessFlag> getAccessFlags();

    public abstract int getNameIndex();

    public abstract int getDescriptorIndex();

    public abstract ImmutableList<AttributeInfo> getAttributeInfos();

    public enum AccessFlag {
        PUBLIC(0x0001),
        PRIVATE(0x0002),
        PROTECTED(0x0004),
        STATIC(0x0008),
        FINAL(0x0010),
        SYNCHRONIZED(0x0020),
        BRIDGE(0x0040),
        VARARGS(0x0080),
        NATIVE(0x0100),
        ABSTRACT(0x0400),
        STRICT(0x0800),
        SYNTHETIC(0x1000),
        ;

        private final int value;

        AccessFlag(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}