#!/bin/bash

set -e

ZSH_DIR="$HOME/.oh-my-zsh"
ZSH_CUSTOM="${ZSH_CUSTOM:-$ZSH_DIR/custom}"

# Install required dependencies if missing
if command -v apt >/dev/null 2>&1; then
  sudo apt update -y
  echo "Checking dependencies (git, curl, zsh)..."

  command -v git >/dev/null 2>&1 || sudo apt install -y git
  command -v curl >/dev/null 2>&1 || sudo apt install -y curl
  command -v zsh >/dev/null 2>&1 || sudo apt install -y zsh

elif command -v brew >/dev/null 2>&1; then
  echo "Checking dependencies (git, curl, zsh)..."

  command -v git >/dev/null 2>&1 || brew install git
  command -v curl >/dev/null 2>&1 || brew install curl
  command -v zsh >/dev/null 2>&1 || brew install zsh
fi

# Install Oh My Zsh if not installed
if [ ! -d "$ZSH_DIR" ]; then
  echo "Installing Oh My Zsh..."
  RUNZSH=no CHSH=yes KEEP_ZSHRC=yes \
  sh -c "$(curl -fsSL https://raw.githubusercontent.com/ohmyzsh/ohmyzsh/master/tools/install.sh)"
else
  echo "Oh My Zsh already installed"
fi

mkdir -p "$ZSH_CUSTOM/plugins"

# Install zsh-autosuggestions plugin
if [ ! -d "$ZSH_CUSTOM/plugins/zsh-autosuggestions" ]; then
  echo "Installing zsh-autosuggestions..."
  git clone https://github.com/zsh-users/zsh-autosuggestions.git \
    "$ZSH_CUSTOM/plugins/zsh-autosuggestions"
else
  echo "zsh-autosuggestions already installed"
fi

# Install zsh-syntax-highlighting plugin
if [ ! -d "$ZSH_CUSTOM/plugins/zsh-syntax-highlighting" ]; then
  echo "Installing zsh-syntax-highlighting..."
  git clone https://github.com/zsh-users/zsh-syntax-highlighting.git \
    "$ZSH_CUSTOM/plugins/zsh-syntax-highlighting"
else
  echo "zsh-syntax-highlighting already installed"
fi

# Ensure plugins are enabled in .zshrc
touch "$HOME/.zshrc"
if grep -q "^plugins=" "$HOME/.zshrc"; then
  sed -i.bak 's/^plugins=.*/plugins=(git zsh-autosuggestions zsh-syntax-highlighting)/' "$HOME/.zshrc"
else
  echo "plugins=(git zsh-autosuggestions zsh-syntax-highlighting)" >> "$HOME/.zshrc"
fi

echo "Reloading shell config..."
source "$HOME/.zshrc" || true

echo "Oh-My-Zsh setup completed successfully!"