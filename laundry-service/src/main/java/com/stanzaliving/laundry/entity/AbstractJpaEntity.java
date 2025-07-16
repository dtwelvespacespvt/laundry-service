package com.stanzaliving.laundry.entity;

import com.stanzaliving.laundry.listener.StanzaEntityListener;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners({StanzaEntityListener.class})
@MappedSuperclass
public abstract class AbstractJpaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "uuid", unique = true, nullable = false)
    protected String uuid;

    @Column(name = "created_by")
    protected String createdBy;

    @Column(name = "updated_by")
    protected String updatedBy;

    @Column(name = "created_at")
    protected Date createdAt;

    @Column(name = "updated_at")
    protected Date updatedAt;

    @Column(name = "status")
    @Builder.Default
    protected boolean status = true;
}
