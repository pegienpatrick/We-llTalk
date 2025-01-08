<template>
    <div>
        <VRow>
            <VCol cols="3" class="text-center">
                <VCard class="w-100">
                    <VCardItem>
                        <VBtn
                        text="New Message"
                        prepend-icon="mdi-plus-box"
                        class="w-100"

                        />
                    </VCardItem>
                </VCard>
                <hr>
                <VCard>
                    <VCardItem>
                        <VBtn
                        text="Create Group"
                        prepend-icon="mdi-account-multiple-plus"
                        class="w-100"

                        />
                    </VCardItem>
                </VCard>
                <hr>
                <div v-for="chat in chats" :key="chat.partnerId">
                <VCard>
                    <VCardItem>
                        {{ chat.profileName }}
                    </VCardItem>
                </VCard>
                <hr>
                </div>


            </VCol>
            <VCol cols="3" class="h-100">
                <VCard class="h-100">
                    <VCardItem class="h-5">
                        <VRow><VCol class="text-center text-bold">
                            {{ activeChat.profileName }}
                        </VCol></VRow>
                        <VRow><VCol class="text-center text-bold">
                            <tt><i>{{ activeChat.status }}</i></tt>
                        </VCol></VRow>


                    </VCardItem>
                    <VCardText class="h-90" style="height:90%">



                    </VCardText>
                    <VCardItem class="align-Bottom d-flex">
                        <VTextarea variant="outlined" height="20px" align-start ></VTextarea>
                        <VBtn
                            icon="mdi-send"
                            align-Center
                            class="text-center m-auto p-0"
                        />

                    </VCardItem>
                </VCard>
                
            </VCol>
        </VRow>
    </div>
</template>
<script setup>
import axios from 'axios';
import Cookies from 'vue-cookies';
import { useRouter } from 'vue-router';
import { ref } from 'vue';

const router = useRouter()

const loadProfile=()=>{

    axios.get('/api/v1/profile/userInfo',{
        headers:{
            Authorization : Cookies.get("Authorization")
        }
    }).then((response)=>{
        console.log(response)
        loadChats();
        loadGroups();

    }).catch((error)=>{
        console.log(error)
        if(error.response.status==403)
            router.push('/auth/login')

    })

}
loadProfile();
const chats=ref([])

const activeChat=ref({
    profileName:'Patrick Gee',
    partnerId:'',
    status:'last seen 5 min ago'
})

const loadChats=()=>{
    axios.get('/api/v1/message/listChats',{
        headers:{
            Authorization : Cookies.get("Authorization")
        }
    })
    .then((response)=>{
        console.log(response)
        chats.value=response.data
        
    }).catch((error)=>{
        console.log(error)
    })
}

const loadGroups=()=>{
    axios.get('/api/v1/groups/listGroups',{
        headers:{
            Authorization : Cookies.get("Authorization")
        }
    })
    .then((response)=>{
        console.log(response)
        
    }).catch((error)=>{
        console.log(error)
    })
}




</script>