<template>
<div class="d-block m-auto"> 
    <div class="auth-wrapper d-flex align-center justify-center pa-4 d-block m-auto text-center">
        <VRow class="d-block m-auto">
            <VCol class="d-block m-auto">
                <VCard
                class="auth-card pa-4 pt-7 justify-center d-block m-auto text-center"
                max-width="448"
                style="background-color: white;"
      
      >
                    <VCardTitle>
                        Register Account
                    </VCardTitle>
                    <VCardText>
                        <VForm class="d-block m-auto" @submit.prevent="registerProfile">
                            <VRow>
                                <VCol cols="12">
                                    <VTextField
                                        label="Profile Name"
                                        variant="outlined"
                                        v-model="registerForm.profileName"
                                        :error="errors.profileName!=null"
                                        :error-messages="errors.profileName"
                                        
                                    />
                                </VCol>

                                <VCol cols="12">
                                    <VTextField
                                        label="Username"
                                        variant="outlined"
                                        v-model="registerForm.username"
                                        :error="errors.username != null"
                                        :error-messages="errors.username"
                                        
                                        
                                    />
                                </VCol>

                                <VCol cols="12">
                                    <VTextField
                                        label="Email"
                                        variant="outlined"
                                        type="email"
                                        v-model="registerForm.email"
                                        :error="errors.email != null"
          
                                        :error-messages="errors.email"
                                        
                                    />
                                </VCol>

                                <VCol cols="12">
                                    <VTextField
                                        label="Phone"
                                        variant="outlined"
                                        v-model="registerForm.phone"
                                        :error="errors.phone != null"
          
                                        :error-messages="errors.phone"
                                        
                                    />
                                </VCol>

                                <VCol cols="12">
                                    <VTextField
                                        label="Password"
                                        variant="outlined"
                                        :append-inner-icon="registerForm.showPassword?'mdi-eye-off':'mdi-eye'"
                                        @click:append-inner="registerForm.showPassword=!registerForm.showPassword"
                                        :type="registerForm.showPassword?'text':'password'"
                                        v-model="registerForm.password"

                                        :error="errors.password != null"
          
                                        :error-messages="errors.password"
                                        
                                        
                                    />
                                </VCol>

                                <VCol cols="12">
                                    <VTextField
                                        label="Confirm Password"
                                        variant="outlined"
                                        :append-inner-icon="registerForm.showPassword?'mdi-eye-off':'mdi-eye'"
                                        @click:append-inner="registerForm.value.showPassword=!registerForm.value.showPassword"
                                        :type="registerForm.showPassword?'text':'password'"
                                        
                                        v-model="registerForm.confirm_password"

                                        :error="errors.confirm_password != null"
        
                                        :error-messages="errors.confirm_password"
                                    />
                                </VCol>

                                <VCol cols="12">
                                    <VBtn
                                        text="Register"
                                       
                                        color="primary"
                                        class=" w-75"
                                        type="submit"
                                        

                                        
                                    />
                                </VCol>

                                <VCol cols="12">
                                    <VBtn
                                        text="Go To Login"
                                        variant="outlined"
                                        to="/login"
                                        
                                    />
                                </VCol>

                            </VRow>
                        </VForm>
                    </VCardText>
                </VCard>
            </VCol>
        </VRow>
    </div>
</div>
</template>
<script setup>

import { ref } from 'vue';
import axios from "axios"
import swal from "sweetalert2"


let apiUrl="http://welltalk.us.to:8081"

const registerForm=ref({
  "profileName": "",
  "username": "",
  "showPassword":false,
  "password": "",
  "email": "",
  "phone": "",
  "confirm_password": "",
  
  
});

const errors=ref({})

const registerProfile = ()=>{
    errors.value={}

    if(registerForm.value.password!=registerForm.value.confirm_password){
        alert('passwords don`t match')
        errors.value.confirm_password="Passwords don`t match"
    }
    console.log(registerForm.value);

    axios.post('/api/v1/profile/registerUser',registerForm.value)
    .then((response)=>{
        console.log(response);
        swal.fire({
            icon: 'success',
            title: 'Registration Success',
            text: 'Account created successfully. Go to Login',
            showConfirmButton: true,
        confirmButtonText: 'Go to Login',
      }).then((result) => {
        if (result.isConfirmed) {
          router.push('/login');
        }
      });


    }).catch((error)=>{
        console.log(error);
        if(error.response.data.fielderrors){
            errors.value=error.response.data.fielderrors;
        
            console.log(errors)
            console.log(errors.value.profileName)
            console.log(errors.value['profileName'])
        } else {
            swal.fire({
                icon: 'error',
                title: 'Registration Error',
                text: error.response.data,
                showConfirmButton: true,
        confirmButtonText: 'Go to Login',
      }).then((result) => {
        if (result.isConfirmed) {
          router.push('/login');
        }
      });
        }

    });






}

</script>
