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
package com.tyron.javacompletion.model;

import androidx.annotation.NonNull;

import com.google.auto.value.AutoValue;

import java.util.Optional;

/**
 * A {@link TypeArgument} starting with question mark (?).
 */
@AutoValue
public abstract class WildcardTypeArgument implements TypeArgument {

    public static WildcardTypeArgument create(Optional<Bound> bound) {
        return new AutoValue_WildcardTypeArgument(bound);
    }

    public static WildcardTypeArgument create(Bound bound) {
        return new AutoValue_WildcardTypeArgument(Optional.of(bound));
    }

    public abstract Optional<Bound> getBound();

    @Override
    public Optional<WildcardTypeArgument> applyTypeParameters(
            SolvedTypeParameters solvedTypeParameters) {
        Optional<Bound> bound = getBound();
        if (!bound.isPresent()) {
            return Optional.empty();
        }
        Optional<TypeReference> typeReference =
                bound.get().getTypeReference().applyTypeParameters(solvedTypeParameters);
        return typeReference.map(reference -> WildcardTypeArgument.create(Bound.create(bound.get().getKind(), reference)));
    }

    @Override
    public String toDisplayString() {
        if (getBound().isPresent()) {
            return "? " + getBound().get().toDisplayString();
        }
        return "?";
    }

    @NonNull
    @Override
    public String toString() {
        return "WildcardTypeArgument<" + getBound() + ">";
    }

    @AutoValue
    public abstract static class Bound {
        public static Bound create(Kind kind, TypeReference typeReference) {
            return new AutoValue_WildcardTypeArgument_Bound(kind, typeReference);
        }

        public abstract Kind getKind();

        public abstract TypeReference getTypeReference();

        public String toDisplayString() {
            return (getKind() == Kind.SUPER ? "super" : "extends")
                    + " "
                    + getTypeReference().toDisplayString();
        }

        @NonNull
        @Override
        public String toString() {
            return "Bound<" + getKind() + " " + getTypeReference() + ">";
        }

        public enum Kind {
            SUPER,
            EXTENDS,
        }
    }
}