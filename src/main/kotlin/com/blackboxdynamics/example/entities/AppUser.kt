package com.blackboxdynamics.example.entities

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity
class AppUser(val firstName:String,
              val lastName:String,
              val email:String,
              private val username:String,
              private var password:String? = null,
) : UserDetails {

    @Id
    @SequenceGenerator(
        name = "user_sequence",
        sequenceName = "user_sequence"
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "user_sequence"
    )
    val id: Long? = null
    @ManyToMany(
        fetch = FetchType.EAGER
    )
    var authorities:MutableList<Authority> = mutableListOf<Authority>()

    var enabled:Boolean = true
    var locked:Boolean = false
    var expired:Boolean = false
    var credentialExpired:Boolean = false

    override fun getUsername(): String {
        return username
    }

    override fun getPassword(): String? {
        return password
    }

    fun setPassword(value:String) {
        password = value;
    }

    override fun isAccountNonExpired(): Boolean {
        return !expired
    }

    override fun isAccountNonLocked(): Boolean {
        return !locked
    }

    override fun isCredentialsNonExpired(): Boolean {
        return !credentialExpired
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return authorities
            .map { authority -> SimpleGrantedAuthority(authority.name.name) }
            .toMutableList()
    }
}