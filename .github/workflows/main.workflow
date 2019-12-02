  
workflow "Build, Test" {
  on = "push"
}

action "Build" {
  uses = "vgaidarji/android-github-actions/build@v1.0.0"
  args = "./gradlew assembleDebug -PpreDexEnable=false"
}

action "Check" {
  needs = ["Build"]
  uses = "vgaidarji/android-github-actions/build@v1.0.0"
  args = "./gradlew testDebug lintDebug -PpreDexEnable=false"
}

action "Run UI Tests" {
  needs = ["Build"]
  uses = "vgaidarji/android-github-actions/emulator@v1.0.0"
}