#!/usr/bin/env python3
"""
Simple script to create basic Bugatti textures using Python PIL
Run: python3 create_textures.py
"""

try:
    from PIL import Image, ImageDraw
    import os
    
    def create_bugatti_item_texture():
        """Create a 16x16 Bugatti car item texture"""
        img = Image.new('RGBA', (16, 16), (0, 0, 0, 0))  # Transparent background
        draw = ImageDraw.Draw(img)
        
        # Bugatti blue body
        blue = (43, 108, 196, 255)  # Bugatti blue
        silver = (192, 192, 192, 255)  # Silver accents
        black = (32, 32, 32, 255)  # Wheels
        light_blue = (173, 216, 230, 120)  # Windows
        
        # Car body (simplified top view)
        draw.rectangle([2, 3, 13, 12], fill=blue)  # Main body
        draw.rectangle([4, 2, 11, 3], fill=light_blue)  # Windshield
        draw.rectangle([4, 12, 11, 13], fill=silver)  # Rear
        
        # Wheels
        draw.rectangle([1, 4, 2, 6], fill=black)  # Front left
        draw.rectangle([13, 4, 14, 6], fill=black)  # Front right
        draw.rectangle([1, 9, 2, 11], fill=black)  # Rear left
        draw.rectangle([13, 9, 14, 11], fill=black)  # Rear right
        
        # Headlights
        draw.rectangle([3, 2, 4, 3], fill=(255, 255, 255, 255))  # Left headlight
        draw.rectangle([11, 2, 12, 3], fill=(255, 255, 255, 255))  # Right headlight
        
        return img
    
    def create_bugatti_entity_texture():
        """Create a 128x64 Bugatti entity texture"""
        img = Image.new('RGBA', (128, 64), (0, 0, 0, 0))
        draw = ImageDraw.Draw(img)
        
        # Bugatti colors
        blue = (43, 108, 196, 255)  # Bugatti blue
        dark_blue = (30, 75, 140, 255)  # Darker blue for shadows
        silver = (192, 192, 192, 255)  # Chrome/silver
        black = (32, 32, 32, 255)  # Wheels and details
        white = (255, 255, 255, 255)  # Lights
        light_blue = (173, 216, 230, 180)  # Windows
        
        # Main body sections (this is a simplified UV mapping)
        # Body sides
        draw.rectangle([0, 0, 64, 32], fill=blue)  # Main body texture
        draw.rectangle([0, 32, 64, 40], fill=dark_blue)  # Lower body
        
        # Roof/top
        draw.rectangle([64, 0, 96, 16], fill=blue)  # Roof
        draw.rectangle([64, 16, 96, 32], fill=light_blue)  # Windows
        
        # Wheels (4 wheel textures)
        for i, x in enumerate([96, 104, 112, 120]):
            draw.rectangle([x, 0, x+8, 8], fill=black)  # Wheel base
            draw.rectangle([x+1, 1, x+7, 7], fill=silver)  # Rim
            draw.rectangle([x+3, 3, x+5, 5], fill=black)  # Center
        
        # Headlights and details
        draw.rectangle([0, 40, 16, 48], fill=white)  # Headlights
        draw.rectangle([16, 40, 32, 48], fill=silver)  # Grille
        draw.rectangle([32, 40, 48, 48], fill=(255, 0, 0, 255))  # Taillights
        
        return img
    
    def create_car_parts():
        """Create textures for car parts"""
        parts = {}
        
        # Car engine - metallic with details
        engine = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
        draw = ImageDraw.Draw(engine)
        draw.rectangle([2, 2, 13, 13], fill=(96, 96, 96, 255))  # Engine block
        draw.rectangle([4, 4, 11, 11], fill=(64, 64, 64, 255))  # Engine details
        draw.rectangle([6, 1, 9, 3], fill=(192, 192, 192, 255))  # Top part
        parts['engine'] = engine
        
        # Car wheel - black with silver rim
        wheel = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
        draw = ImageDraw.Draw(wheel)
        draw.rectangle([2, 2, 13, 13], fill=(32, 32, 32, 255))  # Tire
        draw.rectangle([4, 4, 11, 11], fill=(192, 192, 192, 255))  # Rim
        draw.rectangle([6, 6, 9, 9], fill=(64, 64, 64, 255))  # Center
        parts['wheel'] = wheel
        
        # Car chassis - gray metal frame
        chassis = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
        draw = ImageDraw.Draw(chassis)
        draw.rectangle([1, 1, 14, 14], fill=(128, 128, 128, 255))  # Frame
        draw.rectangle([3, 3, 12, 12], fill=(0, 0, 0, 0))  # Hollow center
        draw.rectangle([1, 7, 14, 8], fill=(96, 96, 96, 255))  # Cross beam
        draw.rectangle([7, 1, 8, 14], fill=(96, 96, 96, 255))  # Cross beam
        parts['chassis'] = chassis
        
        return parts
    
    # Create directories
    os.makedirs('src/main/resources/assets/yourmod/textures/item', exist_ok=True)
    os.makedirs('src/main/resources/assets/yourmod/textures/entity', exist_ok=True)
    
    # Create and save textures
    print("Creating Bugatti textures...")
    
    # Item texture
    item_texture = create_bugatti_item_texture()
    item_texture.save('src/main/resources/assets/yourmod/textures/item/bugatti_car.png')
    print("✓ Created bugatti_car item texture")
    
    # Entity texture
    entity_texture = create_bugatti_entity_texture()
    entity_texture.save('src/main/resources/assets/yourmod/textures/entity/bugatti_car.png')
    print("✓ Created bugatti_car entity texture")
    
    # Car parts
    parts = create_car_parts()
    for part_name, texture in parts.items():
        texture.save(f'src/main/resources/assets/yourmod/textures/item/car_{part_name}.png')
        print(f"✓ Created car_{part_name} texture")
    
    print("\n🎨 All Bugatti textures created successfully!")
    print("Colors used:")
    print("- Bugatti Blue: #2B6CC4")
    print("- Silver/Chrome: #C0C0C0") 
    print("- Black wheels: #202020")
    print("- White lights: #FFFFFF")
    print("- Light blue windows: #ADD8E6")
    
except ImportError:
    print("PIL (Pillow) not installed. Install it with:")
    print("pip3 install Pillow")
    print("\nAlternatively, create the textures manually with these colors:")
    print("- Bugatti Blue: #2B6CC4")
    print("- Silver: #C0C0C0")
    print("- Black: #202020")
    print("- White: #FFFFFF")
    print("- Light Blue: #ADD8E6")
