#!/bin/bash

set -e

ZSH_DIR="$HOME/.oh-my-zsh"
ZSH_CUSTOM="${ZSH_CUSTOM:-$ZSH_DIR/custom}"

echo "Starting Oh My Zsh removal..."

# Remove Oh My Zsh directory
if [ -d "$ZSH_DIR" ]; then
  echo "Removing Oh My Zsh directory..."
  rm -rf "$ZSH_DIR"
else
  echo "Oh My Zsh directory not found."
fi

# Remove plugins if they exist
if [ -d "$ZSH_CUSTOM/plugins/zsh-autosuggestions" ]; then
  echo "Removing zsh-autosuggestions plugin..."
  rm -rf "$ZSH_CUSTOM/plugins/zsh-autosuggestions"
fi

if [ -d "$ZSH_CUSTOM/plugins/zsh-syntax-highlighting" ]; then
  echo "Removing zsh-syntax-highlighting plugin..."
  rm -rf "$ZSH_CUSTOM/plugins/zsh-syntax-highlighting"
fi

# Restore default shell to bash if zsh is current shell
CURRENT_SHELL=$(basename "$SHELL")
if [ "$CURRENT_SHELL" = "zsh" ]; then
  if command -v bash >/dev/null 2>&1; then
    echo "Changing default shell back to bash..."
    chsh -s $(which bash)
  fi
fi

# Remove zsh configuration file
if [ -f "$HOME/.zshrc" ]; then
  echo "Removing .zshrc..."
  rm -f "$HOME/.zshrc"
fi

echo "Oh My Zsh has been successfully removed."
