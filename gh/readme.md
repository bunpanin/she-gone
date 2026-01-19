### Create Access Token in Github
```bash
# On Select scopes
Click repo
click workflow
admin:org
- click read:org
```

### Push
```bash
gh repo create my-project --public
git init
git branch -M main
git add .
git commit -m "Initial commit"
git remote add origin https://github.com/YOUR_USERNAME/my-project.git
git push -u origin main
```
### Clone
```bash
# use gh auth status if the same account 
gh repo clone bunpanin/using-ghh
gh remote -v
```