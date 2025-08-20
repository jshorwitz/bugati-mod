#!/usr/bin/env python3
"""
Authentic Bugatti Car Texture Generator
Creates realistic top-down view 16x16 pixel car textures based on actual Bugatti models
Run: python3 create_authentic_bugatti_textures.py
"""

try:
    from PIL import Image, ImageDraw
    import os
    import math

    def draw_horseshoe_grille(draw, x, y, width, height, fill_color):
        """Draw the iconic Bugatti horseshoe grille shape"""
        # Main horseshoe shape using polygon
        points = [
            (x + width//4, y),  # Top left
            (x + 3*width//4, y),  # Top right
            (x + width, y + height//3),  # Right curve start
            (x + width, y + 2*height//3),  # Right curve end
            (x + 3*width//4, y + height),  # Bottom right
            (x + width//4, y + height),  # Bottom left
            (x, y + 2*height//3),  # Left curve end
            (x, y + height//3),  # Left curve start
        ]
        draw.polygon(points, fill=fill_color)

    def draw_car_outline(draw, body_color, accent_color, wheel_color, width=16, height=16):
        """Draw basic car outline with proper proportions"""
        # Front section (3 pixels)
        draw.rectangle([4, 1, 11, 3], fill=body_color)
        
        # Main body (7 pixels)
        draw.rectangle([2, 4, 13, 10], fill=body_color)
        
        # Rear section (3 pixels)
        draw.rectangle([4, 11, 11, 14], fill=body_color)
        
        # Wheels (positioned realistically)
        # Front wheels
        draw.rectangle([1, 3, 2, 5], fill=wheel_color)  # Front left
        draw.rectangle([13, 3, 14, 5], fill=wheel_color)  # Front right
        # Rear wheels  
        draw.rectangle([1, 10, 2, 12], fill=wheel_color)  # Rear left
        draw.rectangle([13, 10, 14, 12], fill=wheel_color)  # Rear right

    def create_bugatti_chiron():
        """Create Bugatti Chiron texture - Classic elegance"""
        img = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
        draw = ImageDraw.Draw(img)
        
        # Authentic Bugatti Chiron colors
        bugatti_blue = (55, 75, 147, 255)  # #374B93 - Classic Bugatti Blue
        silver_chrome = (220, 220, 220, 255)  # Chrome accents
        dark_wheels = (40, 40, 40, 255)  # Dark wheels
        white_lights = (255, 255, 255, 255)  # Headlights
        light_blue_windows = (140, 180, 220, 180)  # Windows
        
        # Car body with proper proportions
        draw_car_outline(draw, bugatti_blue, silver_chrome, dark_wheels)
        
        # Signature horseshoe grille (front center)
        draw_horseshoe_grille(draw, 6, 1, 4, 2, silver_chrome)
        
        # Windshield and windows
        draw.rectangle([5, 4, 10, 6], fill=light_blue_windows)  # Front windshield
        draw.rectangle([5, 8, 10, 10], fill=light_blue_windows)  # Rear window
        
        # Headlights
        draw.rectangle([3, 1, 4, 2], fill=white_lights)  # Left headlight
        draw.rectangle([11, 1, 12, 2], fill=white_lights)  # Right headlight
        
        # Side mirrors
        draw.rectangle([2, 5, 3, 6], fill=silver_chrome)  # Left mirror
        draw.rectangle([12, 5, 13, 6], fill=silver_chrome)  # Right mirror
        
        # Central spine (Bugatti line)
        draw.rectangle([7, 4, 8, 10], fill=silver_chrome)  # Center line
        
        return img

    def create_bugatti_veyron():
        """Create Bugatti Veyron texture - Racing heritage"""
        img = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
        draw = ImageDraw.Draw(img)
        
        # Veyron colors
        dark_blue = (25, 45, 85, 255)  # Deep racing blue
        silver = (200, 200, 200, 255)  # Silver accents
        white_stripe = (255, 255, 255, 255)  # Racing stripes
        black_wheels = (35, 35, 35, 255)  # Black wheels
        
        # More angular, boxy proportions for Veyron
        draw_car_outline(draw, dark_blue, silver, black_wheels)
        
        # Racing stripes down center
        draw.rectangle([7, 1, 8, 14], fill=white_stripe)  # Center stripe
        
        # Horseshoe grille
        draw_horseshoe_grille(draw, 6, 1, 4, 2, silver)
        
        # Side air intakes (Veyron characteristic)
        draw.rectangle([1, 6, 2, 8], fill=silver)  # Left intake
        draw.rectangle([13, 6, 14, 8], fill=silver)  # Right intake
        
        # Windows
        draw.rectangle([5, 4, 6, 6], fill=(100, 120, 150, 180))  # Left window
        draw.rectangle([9, 4, 10, 6], fill=(100, 120, 150, 180))  # Right window
        
        return img

    def create_bugatti_divo():
        """Create Bugatti Divo texture - Track-focused aggression"""
        img = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
        draw = ImageDraw.Draw(img)
        
        # Divo colors
        matte_gray = (60, 70, 80, 255)  # Matte gray base
        orange_accent = (255, 140, 0, 255)  # Orange racing accents
        black_wheels = (30, 30, 30, 255)  # Matte black wheels
        dark_windows = (40, 50, 60, 180)  # Dark tinted windows
        
        # Angular, aggressive design
        draw_car_outline(draw, matte_gray, orange_accent, black_wheels)
        
        # Aggressive front splitter
        draw.rectangle([3, 0, 12, 1], fill=orange_accent)  # Front splitter
        
        # Orange accent stripes
        draw.rectangle([5, 1, 6, 14], fill=orange_accent)  # Left stripe
        draw.rectangle([9, 1, 10, 14], fill=orange_accent)  # Right stripe
        
        # Rear wing element
        draw.rectangle([4, 14, 11, 15], fill=orange_accent)  # Rear wing
        
        # Angular side vents
        draw.rectangle([0, 7, 1, 9], fill=orange_accent)  # Left vent
        draw.rectangle([14, 7, 15, 9], fill=orange_accent)  # Right vent
        
        # Dark windows
        draw.rectangle([6, 4, 9, 6], fill=dark_windows)  # Windshield
        
        return img

    def create_bugatti_type35():
        """Create Bugatti Type 35 texture - Vintage racing legend"""
        img = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
        draw = ImageDraw.Draw(img)
        
        # Type 35 colors (1920s racing)
        racing_blue = (45, 85, 140, 255)  # Classic racing blue
        brass_details = (184, 134, 11, 255)  # Brass/copper vintage
        cream_number = (255, 248, 220, 255)  # Racing number
        exposed_wheels = (80, 80, 80, 255)  # Exposed wire wheels
        
        # Vintage streamlined oval shape
        # More rounded, classic proportions
        draw.ellipse([3, 2, 12, 13], fill=racing_blue)  # Main oval body
        
        # Exposed wheels (vintage style)
        draw.ellipse([0, 4, 3, 7], fill=exposed_wheels)  # Front left
        draw.ellipse([12, 4, 15, 7], fill=exposed_wheels)  # Front right
        draw.ellipse([0, 8, 3, 11], fill=exposed_wheels)  # Rear left  
        draw.ellipse([12, 8, 15, 11], fill=exposed_wheels)  # Rear right
        
        # Racing number circle on hood
        draw.ellipse([6, 4, 9, 7], fill=cream_number)  # Number circle
        draw.rectangle([7, 5, 8, 6], fill=racing_blue)  # Simple "1" or "35"
        
        # Brass details
        draw.rectangle([4, 2, 11, 3], fill=brass_details)  # Front trim
        draw.rectangle([7, 1, 8, 2], fill=brass_details)  # Hood ornament
        
        # Vintage windscreen
        draw.rectangle([6, 8, 9, 9], fill=(200, 220, 240, 150))  # Small windscreen
        
        return img

    def create_bugatti_centodieci():
        """Create Bugatti Centodieci texture - Ultra-luxury exclusivity"""
        img = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
        draw = ImageDraw.Draw(img)
        
        # Centodieci colors
        pearl_white = (250, 250, 255, 255)  # Pearl white finish
        gold_accent = (255, 215, 0, 255)  # Gold luxury accents
        platinum_wheels = (220, 220, 235, 255)  # Platinum wheels
        crystal_blue = (200, 230, 255, 200)  # Crystal-inspired windows
        
        # Ultra-clean, minimal design
        draw_car_outline(draw, pearl_white, gold_accent, platinum_wheels)
        
        # Gold horseshoe grille
        draw_horseshoe_grille(draw, 6, 1, 4, 2, gold_accent)
        
        # Premium crystal windows
        draw.rectangle([5, 4, 10, 6], fill=crystal_blue)  # Front windshield
        draw.rectangle([5, 8, 10, 10], fill=crystal_blue)  # Rear window
        
        # Gold accent lines (luxury details)
        draw.rectangle([4, 7, 11, 8], fill=gold_accent)  # Side accent line
        
        # Premium details
        draw.rectangle([3, 1, 4, 2], fill=gold_accent)  # Left headlight trim
        draw.rectangle([11, 1, 12, 2], fill=gold_accent)  # Right headlight trim
        
        # Subtle luxury body lines
        draw.rectangle([3, 5, 4, 6], fill=gold_accent)  # Left body line
        draw.rectangle([11, 5, 12, 6], fill=gold_accent)  # Right body line
        
        return img

    def create_bugatti_bolide():
        """Create Bugatti Bolide texture - Extreme racing machine"""
        img = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
        draw = ImageDraw.Draw(img)
        
        # Bolide colors
        matte_black = (25, 25, 25, 255)  # Matte black base
        neon_cyan = (0, 255, 255, 255)  # Neon cyan accents
        orange_flame = (255, 100, 0, 255)  # Orange racing details
        carbon_wheels = (40, 40, 45, 255)  # Carbon fiber wheels
        
        # Aggressive angular racing design
        draw_car_outline(draw, matte_black, neon_cyan, carbon_wheels)
        
        # Extreme aerodynamic elements
        # Front splitter with canards
        draw.rectangle([2, 0, 13, 1], fill=orange_flame)  # Front splitter
        draw.rectangle([1, 1, 2, 2], fill=neon_cyan)  # Left canard
        draw.rectangle([13, 1, 14, 2], fill=neon_cyan)  # Right canard
        
        # Neon accent lines
        draw.rectangle([4, 3, 11, 4], fill=neon_cyan)  # Hood line
        draw.rectangle([4, 11, 11, 12], fill=neon_cyan)  # Rear line
        
        # Racing side vents
        draw.rectangle([0, 6, 2, 8], fill=orange_flame)  # Left side vent
        draw.rectangle([13, 6, 15, 8], fill=orange_flame)  # Right side vent
        
        # Massive rear wing
        draw.rectangle([3, 13, 12, 15], fill=neon_cyan)  # Rear wing
        
        # Racing windscreen
        draw.rectangle([6, 5, 9, 7], fill=(50, 100, 150, 150))  # Racing windscreen
        
        return img

    def save_texture_with_info(texture, model_name, colors_info):
        """Save texture and create accompanying info file"""
        # Save texture
        texture_path = f'src/main/resources/assets/yourmod/textures/item/bugatti_{model_name}.png'
        texture.save(texture_path)
        
        # Save color info
        colors_path = f'src/main/resources/assets/yourmod/textures/item/bugatti_{model_name}_colors.txt'
        with open(colors_path, 'w') as f:
            f.write(f"# Bugatti {model_name.title()} Texture Colors\n\n")
            for color_name, hex_code in colors_info.items():
                f.write(f"{color_name}: {hex_code}\n")
        
        print(f"✓ Created authentic {model_name} texture with proper car proportions")

    # Create directories
    os.makedirs('src/main/resources/assets/yourmod/textures/item', exist_ok=True)
    os.makedirs('src/main/resources/assets/yourmod/textures/entity', exist_ok=True)
    
    print("🏎️  Creating Authentic Bugatti Car Textures...")
    print("Each texture is designed to look like an actual car from top-down view\n")
    
    # Create all Bugatti models with authentic designs
    models = [
        ('chiron', create_bugatti_chiron(), {
            'Bugatti Blue': '#374B93',
            'Silver Chrome': '#DCDCDC', 
            'Dark Wheels': '#282828',
            'White Lights': '#FFFFFF',
            'Light Blue Windows': '#8CB4DC'
        }),
        ('veyron', create_bugatti_veyron(), {
            'Dark Racing Blue': '#192D55',
            'Silver Accents': '#C8C8C8',
            'White Racing Stripe': '#FFFFFF',
            'Black Wheels': '#232323'
        }),
        ('divo', create_bugatti_divo(), {
            'Matte Gray': '#3C4650',
            'Orange Racing Accent': '#FF8C00',
            'Matte Black Wheels': '#1E1E1E',
            'Dark Windows': '#283C50'
        }),
        ('type35', create_bugatti_type35(), {
            'Classic Racing Blue': '#2D558C',
            'Vintage Brass': '#B8860B',
            'Cream Racing Number': '#FFF8DC',
            'Exposed Wire Wheels': '#505050'
        }),
        ('centodieci', create_bugatti_centodieci(), {
            'Pearl White': '#FAFAFF',
            'Luxury Gold': '#FFD700',
            'Platinum Wheels': '#DCDCEB',
            'Crystal Blue Windows': '#C8E6FF'
        }),
        ('bolide', create_bugatti_bolide(), {
            'Matte Black': '#191919',
            'Neon Cyan': '#00FFFF',
            'Orange Racing Flame': '#FF6400',
            'Carbon Fiber Wheels': '#28282D'
        })
    ]
    
    for model_name, texture, colors in models:
        save_texture_with_info(texture, model_name, colors)
    
    # Create updated README
    readme_content = """# Authentic Bugatti Car Textures

These 16x16 pixel textures are designed to look like actual Bugatti cars from a top-down view.

## Design Features:
- Proper car proportions (front, body, rear sections)
- Realistic wheel positioning
- Iconic Bugatti horseshoe grille
- Model-specific authentic colors and details
- Windows, mirrors, and body lines
- Aerodynamic elements where appropriate

## Models:

1. **Bugatti Chiron** - Classic elegant flagship
   - Signature Bugatti blue with chrome accents
   - Clean, premium design with central spine

2. **Bugatti Veyron** - Racing heritage icon  
   - Deep blue with white racing stripes
   - Distinctive side air intakes

3. **Bugatti Divo** - Track-focused aggression
   - Matte gray with orange racing accents
   - Angular design with aerodynamic elements

4. **Bugatti Type 35** - Vintage racing legend
   - Classic 1920s racing blue
   - Exposed wire wheels and racing number

5. **Bugatti Centodieci** - Ultra-luxury exclusivity
   - Pearl white with gold luxury accents
   - Clean, minimal premium design

6. **Bugatti Bolide** - Extreme racing machine
   - Matte black with neon cyan accents
   - Aggressive aerodynamic racing elements

Each texture captures the essence of these iconic supercars even at 16x16 resolution.
"""
    
    with open('src/main/resources/assets/yourmod/textures/item/AUTHENTIC_BUGATTI_README.txt', 'w') as f:
        f.write(readme_content)
    
    print(f"\n🎨 All 6 authentic Bugatti textures created successfully!")
    print("Each texture now looks like an actual car from above with:")
    print("- Proper automotive proportions")
    print("- Iconic Bugatti horseshoe grille")
    print("- Model-specific authentic colors")
    print("- Realistic wheel positioning")
    print("- Windows, mirrors, and design details")
    print("\n📁 Files created in: src/main/resources/assets/yourmod/textures/item/")

except ImportError:
    print("❌ PIL (Pillow) not installed. Install it with:")
    print("pip3 install Pillow")
    print("\nOr install using conda:")
    print("conda install pillow")
