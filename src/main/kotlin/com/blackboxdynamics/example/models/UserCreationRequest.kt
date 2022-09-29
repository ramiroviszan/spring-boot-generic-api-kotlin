package com.blackboxdynamics.example.models

import com.blackboxdynamics.example.config.NoArg

@NoArg
data class UserCreationRequest(val username:String,
                               val password:String,
                               val firstName:String,
                               val lastName:String,
                               val email:String
)