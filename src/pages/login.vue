<template>
    <div>
        <div class="auth-wrapper d-flex align-center justify-center pa-4">
            <VRow>
                <VCol>
                    <VCard
                    class="auth-card pa-4 pt-7 justify-center d-block text-center"
                    max-width="448"
                    style="background-color: white;"
          
          >
                        <VCardTitle>
                            Login Account
                        </VCardTitle>
                        <VCardText>
                            <VForm @submit.prevent="loginUser">
                                <VRow>
                                    <VCol cols="12">
                                        <VTextField
                                            label="Username"
                                            variant="outlined"
                                            v-model="loginForm.username"
                                            :error="errors.username != null"
                                            :error-messages="errors.username"
                                            
                                            
                                        />
                                    </VCol>
        
                                    <VCol cols="12">
                                        <VTextField
                                            label="password"
                                            variant="outlined"
                                            :append-inner-icon="loginForm.showPassword?'mdi-eye-off':'mdi-eye'"
                                            @click:append-inner="loginForm.showPassword=!loginForm.showPassword"
                                            :type="loginForm.showPassword?'text':'password'"
                                            v-model="loginForm.password"

                                            :error="errors.password != null"
            
                                            :error-messages="errors.password"
                                            
                                        />
                                    </VCol>
                                    <VCol cols="12">
                                        <VBtn
                                        text="Login"
                                        color="primary"
                                        type="submit"
                                       
                                        


                                        />


                                        
                                    </VCol>

                                    <VCol cols="12">
                                        <VBtn
                                        text="New Here ? Register An Account"
                                        color="warning"
                                        variant="outlined"
                                        to="/register"
                                        
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
import Cookies from 'vue-cookies'
import axios from 'axios'

import swal from 'sweetalert2'

import { useRouter } from 'vue-router'

const router= useRouter();

const errors=ref({})


    const loginForm=ref({
        username:'',
        password:'',
        showPassword:false,
    })
    

    const loginUser=()=>{
        errors.value={}
        axios.post('/api/v1/profile/login',loginForm.value)
        .then((response)=>{
            console.log(response)
            Cookies.set('Authorization',response.data.authorization)
            swal.fire({
                    icon: 'success',
                    title: 'Login Success',
                    text: response.data.message,
                    timer: 2000, 
                    timerProgressBar: true,
                    showConfirmButton: false,
                    willClose: () => {
                        router.push("/")
                    }
                })

        }).catch((error)=>{
            console.log(error)
            if(error.response.data.message&&JSON.parse(error.response.data.message).fielderrors){
                errors.value=JSON.parse(error.response.data.message).fielderrors;
                console.log(errors.value)
            } 

            if(error.response.status==403)
            {
                swal.fire({
                    icon: 'error',
                    title: 'Invalid Login',
                    text: 'Incorrect Login Details'
                })
            }

            
        })
    }


    </script>
    