/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.speedment.plugins.reactor.internal.component;

import com.speedment.common.injector.annotation.Inject;
import com.speedment.generator.component.TypeMapperComponent;
import static com.speedment.plugins.reactor.internal.util.ReactorComponentUtil.validMergingColumns;
import com.speedment.runtime.config.Column;
import com.speedment.tool.config.TableProperty;
import com.speedment.tool.property.PropertyEditor;
import com.speedment.tool.property.item.ChoiceBoxItem;
import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;
import static javafx.collections.FXCollections.observableList;
import static javafx.collections.FXCollections.observableList;

/**
 *
 * @author Simon
 */
class MergeOnEditor<T extends TableProperty> implements PropertyEditor<T>{

    private @Inject TypeMapperComponent typeMappers;
        
    @Override
    public Stream<Item> fieldsFor(T document) {
        
        return Stream.of(
            new ChoiceBoxItem<>(
                "Merge event on", 
                document.stringPropertyOf(ReactorComponentImpl.MERGE_ON, () -> null),
                observableList(
                    validMergingColumns(document, typeMappers)
                        .stream()
                        .map(Column::getJavaName)
                        .collect(toList())
                ),
                "This column will be used to merge events in a " + 
                "materialized object view (MOV) so that only the " + 
                "most recent revision of an entity is visible."
            )
        );
    }
}