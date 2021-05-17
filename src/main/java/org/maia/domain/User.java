package org.maia.domain;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class User {

    private String identification;
    private String nome;
    private String email;

}
