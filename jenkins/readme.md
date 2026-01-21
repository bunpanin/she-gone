### Install Plugin 
- Stage View
- Blue Ocean
- Node

// stage('Telegram Message') {
//     steps {
//         script {
//             withCredentials([usernamePassword(credentialsId: 'TELEGRAM_BOT', passwordVariable: 'CHAT_ID', usernameVariable: 'TOKEN')]) {
//             sh """
//             curl -s -X POST https://api.telegram.org/bot${TOKEN}/sendMessage \
//                 -d chat_id="${CHAT_ID}" \
//                 -d text="Hello from Jenkins!"
//             """
//             }
//         }
//     }
// }