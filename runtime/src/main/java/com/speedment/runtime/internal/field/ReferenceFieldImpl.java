/**
 *
 * Copyright (c) 2006-2016, Speedment, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); You may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.speedment.runtime.internal.field;

import com.speedment.runtime.config.Column;
import com.speedment.runtime.config.Project;
import com.speedment.runtime.config.mapper.TypeMapper;
import com.speedment.runtime.config.identifier.FieldIdentifier;
import com.speedment.runtime.field.ReferenceField;
import com.speedment.runtime.field.method.FieldSetter;
import com.speedment.runtime.field.method.Getter;
import com.speedment.runtime.field.method.Setter;
import com.speedment.runtime.field.predicate.SpeedmentPredicate;
import com.speedment.runtime.field.trait.FieldTrait;
import com.speedment.runtime.field.trait.ReferenceFieldTrait;
import com.speedment.runtime.internal.field.trait.FieldTraitImpl;
import com.speedment.runtime.internal.field.trait.ReferenceFieldTraitImpl;
import com.speedment.runtime.internal.util.document.DocumentDbUtil;
import static com.speedment.runtime.util.NullUtil.requireNonNulls;
import java.util.Optional;

/**
 * This class represents a Reference Field. A Reference Field is something that
 * extends {@link Object}.
 *
 * @author pemi
 * @param <ENTITY> The entity type
 * @param <V> The value type
 */
public class ReferenceFieldImpl<ENTITY, D, V> implements ReferenceField<ENTITY, D, V> {

    private final FieldTrait field;
    private final ReferenceFieldTrait<ENTITY, D, V> referenceField;

    public ReferenceFieldImpl(
            FieldIdentifier<ENTITY> identifier,
            Getter<ENTITY, V> getter,
            Setter<ENTITY, V> setter,
            TypeMapper<D, V> typeMapper,
            boolean unique
    ) {
        requireNonNulls(identifier, getter, setter, typeMapper);
        field = new FieldTraitImpl(identifier, unique);
        referenceField = new ReferenceFieldTraitImpl<>(field, getter, setter, typeMapper);
    }

    @Override
    public FieldIdentifier<ENTITY> getIdentifier() {
        return referenceField.getIdentifier();
    }
    
    @Override
    public boolean isUnique() {
        return field.isUnique();
    }
    
    @Override
    public Optional<Column> findColumn(Project project) {
        return Optional.of(DocumentDbUtil.referencedColumn(project, getIdentifier()));
    }

    @Override
    public Setter<ENTITY, V> setter() {
        return referenceField.setter();
    }

    @Override
    public Getter<ENTITY, V> getter() {
        return referenceField.getter();
    }

    @Override
    public TypeMapper<D, V> typeMapper() {
        return referenceField.typeMapper();
    }

    @Override
    public FieldSetter<ENTITY, V> setTo(V value) {
        return referenceField.setTo(value);
    }

    @Override
    public SpeedmentPredicate<ENTITY, D, V> isNull() {
        return referenceField.isNull();
    }

    @Override
    public SpeedmentPredicate<ENTITY, D, V> isNotNull() {
        return referenceField.isNotNull();
    }

}