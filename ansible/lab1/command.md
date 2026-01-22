### Lab1
```ini
# Node
-i = inventory
-m = module
-a = argument
state=present  → install
state=absent   → remove
state=latest   → install/update to latest version
--become → Run command with privilege escalation , Same as sudo
--become → sudo 

ansible -i inventory.ini localhost -m ping
ansible -i inventory.ini dev1 -m ping
ansible -i inventory.ini prod1 -m ping
ansible -i inventory.ini all -m ping

# Run add-hoc command module
ansible -i inventory.ini all -m command -a "uptime"
ansible -i inventory.ini all -m apt -a "name=nginx state=present"
ansible -i inventory.ini all -m apt -a "name=nginx state=present" --become




```