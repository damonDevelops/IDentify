echo "Navigate to git.identify.rodeo > User Icon > Settings > SSH / GPG Keys > Add Key to add this key to your account."

mkdir ~/.git
ssh-keygen -b 2048 -t rsa -f ~/.git/idrsa -q -N ""
cat ~/.git/idrsa.pub

read -p "Press enter once the key has been imported"

git remote set-url origin ssh://git@51.161.201.125:8880/IDentify/IdentifyFrontend.git
git config --global user.email $1
git config --global user.name $2
git pull

cd package/main
npm install
