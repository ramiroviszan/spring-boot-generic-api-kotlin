package com.blackboxdynamics.example.entities

import javax.persistence.*

enum class AuthorityName {
    ROLE_USER, ROLE_ADMIN
}

@Entity
@Table(
    name = "Authority",
    uniqueConstraints = [
        UniqueConstraint(
            name = "unique_name_constraint",
            columnNames = ["name"]
        )
    ]
)
data class Authority(
    @Id
    @Column(name = "name")
    val name: AuthorityName) {

    @ManyToMany(mappedBy = "authorities")
    var users = mutableListOf<AppUser>()
}