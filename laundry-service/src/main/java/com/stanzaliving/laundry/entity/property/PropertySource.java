package com.stanzaliving.laundry.entity.property;

import com.stanzaliving.laundry.entity.AbstractJpaEntity;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "property_source")
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonStringType.class),
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class PropertySource extends AbstractJpaEntity {

    @Column(name = "key", unique = true, nullable = false)
    private String key;

    @Column(name = "value", nullable = false, columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private Object value;

    @Column(name = "description")
    private String description;
}
