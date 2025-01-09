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
                        @click="showUserSelectionPopup"
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
                        @click="createGroup"
                        />
                    </VCardItem>
                </VCard>
                <hr>
                <div v-for="chat in chats" :key="chat.partnerId">
                <VCard @click="openChat(chat)">
                    <VCardItem>
                        {{ chat.profileName }}
                    </VCardItem>
                    <VCardItem>
                        <i>{{ chat.status }}</i>
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
                        <VCard v-for="message in cChats" :key="message.uid" class="mt-2" >
                            <VCardText><p>{{ message. messageContent }}</p></VCardText>
                            <VCardItem><i>{{ (new Date(parseInt(message.createDate)*1000)).toLocaleString('en-US', { hour12: false }) }}</i></VCardItem>
                        </VCard>


                    </VCardText>
                    <VCardItem class="align-Bottom d-flex">
                        <VForm @submit.prevent="sendMessage">
                        <VTextarea variant="outlined" height="20px" align-start v-model="nMessage.content"></VTextarea>
                        <VBtn
                            icon="mdi-send"
                            align-Center
                            class="text-center m-auto p-0"
                            type="submit"
                        />
                    </VForm>

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
import Swal from 'sweetalert2';

const router = useRouter()
const profile=ref(null)
const users=ref([])
const loadProfile=()=>{

    axios.get('/api/v1/profile/userInfo',{
        headers:{
            Authorization : Cookies.get("Authorization")
        }
    }).then((response)=>{
        console.log(response)
        profile.value=response.data
        loadChats();
        loadGroups();

    }).catch((error)=>{
        console.log(error)
        if(error.response.status==403)
            router.push('/auth/login')

    })

}

const nMessage=ref({
  "content": "",
  "destinationType": "",
  "recipient": ""
})

loadProfile();
const chats=ref([])

const activeChat=ref({
    profileName:'Patrick Gee',
    partnerId:'',
    status:'last seen 5 min ago'
})

const cChats=ref([])

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


const loadCurrentChat=()=>{
    axios.get(`/api/v1/message/listMessages/${activeChat.value.uid}`,{
        headers:{
            Authorization : Cookies.get("Authorization")
        }
    }).then((response)=>{
        console.log(response)
        cChats.value=response.data;
    })
}


const sendMessage=()=>{
    nMessage.value.recipient=activeChat.value.uid;
    nMessage.value.destinationType='INDIVIDUAL'
    axios.post('/api/v1/message/textMessage',nMessage.value,{
        headers:{
            Authorization : Cookies.get("Authorization")
        }
    }).then((response)=>{
        console.log(response)
        loadCurrentChat();
    })
    
}

const createGroup=()=>{
    Swal.fire({
        title: 'Create a New Group',
        html:
            '<input id="swal-input1" class="swal2-input" placeholder="Group Name">' +
            '<textarea id="swal-input2" class="swal2-textarea" placeholder="Group Description"></textarea>',
        focusConfirm: false,
        showCancelButton: true,
        confirmButtonText: 'Create',
        cancelButtonText: 'Cancel',
        preConfirm: () => {
            const groupName = document.getElementById('swal-input1').value.trim();
            const groupDescription = document.getElementById('swal-input2').value.trim();

            if (!groupName) {
                Swal.showValidationMessage('Group Name is required');
                return false;
            }

            if (!groupDescription) {
                Swal.showValidationMessage('Group Description is required');
                return false;
            }

            return { groupName, groupDescription };
        }
    }).then((result) => {
        if (result.isConfirmed && result.value) {
            const { groupName, groupDescription } = result.value;
            let groupForm={
                "title": groupName,
                "description": groupDescription,
                "groupType": "PRIVATE"
            };
            axios.post('/api/v1/groups/create',groupForm,{
                headers:{
                    Authorization : Cookies.get("Authorization")
                }
            })
            .then((response)=>{
                console.log(response);
                loadChats();
            }).catch((error)=>{
                if(error.response.data)
                    alert(error.response.data)

                if(error.response.error)
                    alert(error.response.error)
            })
        }
    });

}

const  loadUsers= async ()=>{
    var response = await axios.get('/api/v1/profile/listUsers',{
        headers:{
            Authorization : Cookies.get("Authorization")
        }
    })
    // .then((response)=>{
        console.log(response)
        users.value=response.data;
    //         console.log(users)sssss
    // })
}



// Function to initialize the Swal popup
async function showUserSelectionPopup() {
    var i=await loadUsers();
    // var usersdt='';
    // console.log(users)
    // for(var i=0;i<users.value.length;i++)
    // {
    //     var user=users.value[i]
    //     usersdt=`<div class="swal-user-item" data-username="${user.username}" style="padding: 8px; cursor: pointer; border-bottom: 1px solid #eee;">${user.profileName} (@${user.username})</div>`
    // }
    // console.log(usersdt)
  Swal.fire({
    title: 'Select a User',
    html:
      '<input id="swal-search" class="swal2-input" placeholder="Search users...">' +
      '<div id="swal-user-list" style="max-height: 200px; overflow-y: auto; border: 1px solid #ccc; border-radius: 4px; padding: 5px;">' +
      users.value
        .map(
          (user) =>
            `<div class="swal-user-item" data-username="${user.username}" style="padding: 8px; cursor: pointer; border-bottom: 1px solid #eee;">${user.profileName} (@${user.username})</div>`
        )
        .join('') +
    

// usersdt+
      '</div>',
    showCancelButton: true,
    confirmButtonText: 'Select',
    cancelButtonText: 'Cancel',
    focusConfirm: false,
    preConfirm: () => {
      const selectedUser = Swal.getPopup().querySelector('.selected');
      if (!selectedUser) {
        Swal.showValidationMessage('Please select a user.');
        return false;
      }
      const username = selectedUser.getAttribute('data-username');
      const name = selectedUser.textContent;
      return { username, name };
    },
    didOpen: () => {
      const searchInput = Swal.getPopup().querySelector('#swal-search');
      const userList = Swal.getPopup().querySelector('#swal-user-list');
      const userItems = Swal.getPopup().querySelectorAll('.swal-user-item');
      let selectedElement = null;

      // Function to filter user list based on search input
      function filterUsers() {
        const searchTerm = searchInput.value.toLowerCase();
        userItems.forEach((item) => {
          const text = item.textContent.toLowerCase();
          if (text.includes(searchTerm)) {
            item.style.display = '';
          } else {
            item.style.display = 'none';
          }
        });
      }

      // Event listener for search input
      searchInput.addEventListener('input', filterUsers);

      // Event listeners for user item clicks
      userItems.forEach((item) => {
        item.addEventListener('click', () => {
          // Remove 'selected' class from previously selected item
          if (selectedElement) {
            selectedElement.classList.remove('selected');
            selectedElement.style.backgroundColor = '';
          }

          // Add 'selected' class to the clicked item
          selectedElement = item;
          selectedElement.classList.add('selected');
          selectedElement.style.backgroundColor = '#e0e0e0'; // Highlight selected
        });
      });
    },
  }).then((result) => {
    if (result.isConfirmed && result.value) {
      const { username, name } = result.value;
      axios.get(`/api/v1/profile/searchUser/${username}`,{
        headers:{
            Authorization : Cookies.get("Authorization")
        }
    }).then((response)=>{
        activeChat.value=response.data;
        loadCurrentChat();
    })

    }
});
}


const openChat=(chat)=>{
    axios.get(`/api/v1/profile/searchUser/${chat.username}`,{
        headers:{
            Authorization : Cookies.get("Authorization")
        }
    }).then((response)=>{
        activeChat.value=response.data;
        loadCurrentChat();
    })
}



</script>