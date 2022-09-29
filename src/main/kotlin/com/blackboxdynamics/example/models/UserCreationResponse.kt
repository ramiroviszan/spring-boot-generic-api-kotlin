package com.blackboxdynamics.example.models

import com.blackboxdynamics.example.config.NoArg
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

@NoArg
data class UserCreationResponse(val id:Long,
                                val username:String,
                                val firstName:String,
                                val lastName:String,
                                val email:String,
                                var grantedAuthorities:MutableCollection<out GrantedAuthority>
) 