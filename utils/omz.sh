#!/bin/bash

set -e

echo "Installing Oh My Zsh..."
RUNZSH=no CHSH=yes KEEP_ZSHRC=yes \
sh -c "$(curl -fsSL https://raw.githubusercontent.com/ohmyzsh/ohmyzsh/master/tools/install.sh)"

ZSH_CUSTOM="${ZSH_CUSTOM:-$HOME/.oh-my-zsh/custom}"
echo "Installing plugins..."

if [ ! -d "$ZSH_CUSTOM/plugins/zsh-autosuggestions" ]; then
  git clone https://github.com/zsh-users/zsh-autosuggestions.git \
    "$ZSH_CUSTOM/plugins/zsh-autosuggestions"
fi

if [ ! -d "$ZSH_CUSTOM/plugins/zsh-syntax-highlighting" ]; then
  git clone https://github.com/zsh-users/zsh-syntax-highlighting.git \
    "$ZSH_CUSTOM/plugins/zsh-syntax-highlighting"
fi

echo "Adding plugins in .zshrc..."
sed -i.bak \
  's/^plugins=(.*)/plugins=(git zsh-autosuggestions zsh-syntax-highlighting)/' \
  "$HOME/.zshrc"

source "$HOME/.zshrc"

echo "Oh-My-Zsh-installed-successfully!"