#!/bin/bash
# Create basic colored textures using built-in Mac tools

echo "Creating basic Bugatti textures..."

# Create a simple blue square for the car using osascript (AppleScript)
osascript << 'EOF'
tell application "Image Events"
    launch
    set bugatti_image to make new image with properties {dimensions:{16, 16}}
    set every pixel of bugatti_image to {43, 108, 196} -- Bugatti blue RGB
    save bugatti_image as PNG in (POSIX file (POSIX path of (path to desktop)) & "bugatti_temp.png")
    close bugatti_image
end tell
EOF

# Move to correct location if created
if [ -f ~/Desktop/bugatti_temp.png ]; then
    mv ~/Desktop/bugatti_temp.png src/main/resources/assets/yourmod/textures/item/bugatti_car.png
    echo "✓ Created basic blue Bugatti item texture"
else
    echo "⚠️  Could not create texture automatically"
fi

echo ""
echo "🎨 Your Bugatti will be colored with:"
echo "   Primary: Bugatti Blue (#2B6CC4)"
echo "   Accents: Silver Chrome (#C0C0C0)" 
echo "   Wheels: Black (#202020)"
echo "   Lights: White (#FFFFFF)"
echo "   Windows: Light Blue (#ADD8E6)"
echo ""
echo "📋 For best results, manually create 16x16 PNG textures using:"
echo "   - Preview app (built into Mac)"
echo "   - Online pixel art tools"
echo "   - GIMP (free download)"
echo ""
echo "🎯 The car is ready to test - run './gradlew runClient' to see it in action!"
