## NOTE 
Goal:  use ansible to create GCP instance based on our requirement 

Ansible -> create google instance 
Ansible -> authenticated ( IAM -> `serviceaccount.json` file )
        -> success -> create google instance
         

### Slurp does
```bash
# module Slurp
 - name: Slurp the local public key
      slurp: 
        src: "{{ ssh_public_key_path }}" 
      register: ssh_pub_key

# The Reason ssh_pub_key['content'] because it store
metadata:
        ssh-keys: "kk:{{ ssh_pub_key['content'] | b64decode }}"

# ssh_pub_key
ssh_pub_key:
  changed: false
  encoding: base64
  content: "c3NoLXJzYSBBQUFBQjNOemF..."
  source: /home/kk/.ssh/id_rsa.pub
```