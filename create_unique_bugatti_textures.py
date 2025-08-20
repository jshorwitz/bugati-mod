#!/usr/bin/env python3
"""
Advanced Bugatti texture generator with unique designs for each model
Run: python3 create_unique_bugatti_textures.py
"""

try:
    from PIL import Image, ImageDraw
    import os
    
    # Color palettes for each model
    BUGATTI_MODELS = {
        'chiron': {
            'name': 'Bugatti Chiron',
            'colors': {
                'primary': (43, 108, 196, 255),    # Classic Bugatti blue
                'secondary': (192, 192, 192, 255),  # Silver chrome
                'accent': (255, 255, 255, 255),     # White highlights
                'dark': (25, 35, 50, 255),          # Dark blue details
                'window': (173, 216, 230, 180)      # Light blue windows
            },
            'style': 'elegant'
        },
        'veyron': {
            'name': 'Bugatti Veyron', 
            'colors': {
                'primary': (20, 60, 140, 255),      # Deep blue
                'secondary': (32, 32, 32, 255),     # Carbon fiber black
                'accent': (255, 255, 255, 255),     # White racing stripes
                'dark': (15, 15, 15, 255),          # Pure black
                'window': (120, 150, 190, 150)      # Blue-tinted windows
            },
            'style': 'classic_racing'
        },
        'divo': {
            'name': 'Bugatti Divo',
            'colors': {
                'primary': (40, 50, 80, 255),       # Matte dark blue
                'secondary': (255, 140, 0, 255),    # Orange accents
                'accent': (255, 255, 0, 255),       # Yellow details
                'dark': (20, 20, 20, 255),          # Matte black
                'window': (80, 80, 120, 200)        # Dark tinted windows
            },
            'style': 'aggressive'
        },
        'type35': {
            'name': 'Bugatti Type 35',
            'colors': {
                'primary': (60, 100, 160, 255),     # Classic racing blue
                'secondary': (184, 115, 51, 255),   # Brass/copper
                'accent': (218, 165, 32, 255),      # Gold details
                'dark': (101, 67, 33, 255),         # Bronze
                'window': (0, 0, 0, 0)              # No windows (open cockpit)
            },
            'style': 'vintage'
        },
        'centodieci': {
            'name': 'Bugatti Centodieci',
            'colors': {
                'primary': (248, 248, 255, 255),    # Pearl white
                'secondary': (255, 215, 0, 255),    # Gold accents
                'accent': (230, 230, 250, 255),     # Platinum highlights
                'dark': (169, 169, 169, 255),       # Silver details
                'window': (200, 200, 255, 100)      # Crystal-clear windows
            },
            'style': 'luxury'
        },
        'bolide': {
            'name': 'Bugatti Bolide',
            'colors': {
                'primary': (20, 20, 20, 255),       # Matte black
                'secondary': (0, 255, 255, 255),    # Neon cyan
                'accent': (255, 69, 0, 255),        # Orange flame
                'dark': (10, 10, 10, 255),          # Pure black
                'window': (0, 100, 100, 150)        # Tinted cyan
            },
            'style': 'racing_extreme'
        }
    }
    
    def create_chiron_texture():
        """Elegant flagship design"""
        img = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
        draw = ImageDraw.Draw(img)
        colors = BUGATTI_MODELS['chiron']['colors']
        
        # Elegant body shape
        draw.rectangle([2, 4, 13, 11], fill=colors['primary'])
        draw.rectangle([3, 3, 12, 4], fill=colors['window'])
        draw.rectangle([3, 11, 12, 12], fill=colors['secondary'])
        
        # Chrome accents
        draw.rectangle([1, 5, 2, 6], fill=colors['secondary'])  # Side chrome
        draw.rectangle([13, 5, 14, 6], fill=colors['secondary'])
        draw.rectangle([1, 8, 2, 9], fill=colors['secondary'])
        draw.rectangle([13, 8, 14, 9], fill=colors['secondary'])
        
        # Signature horseshoe grille
        draw.rectangle([6, 2, 9, 3], fill=colors['secondary'])
        draw.point((7, 2), fill=colors['dark'])
        
        # Premium wheels
        draw.rectangle([1, 4, 2, 6], fill=colors['dark'])
        draw.rectangle([13, 4, 14, 6], fill=colors['dark'])
        draw.rectangle([1, 9, 2, 11], fill=colors['dark'])
        draw.rectangle([13, 9, 14, 11], fill=colors['dark'])
        
        # Elegant headlights
        draw.point((3, 3), fill=colors['accent'])
        draw.point((12, 3), fill=colors['accent'])
        
        return img
    
    def create_veyron_texture():
        """Classic with racing stripes"""
        img = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
        draw = ImageDraw.Draw(img)
        colors = BUGATTI_MODELS['veyron']['colors']
        
        # Classic body
        draw.rectangle([2, 3, 13, 12], fill=colors['primary'])
        draw.rectangle([4, 2, 11, 3], fill=colors['window'])
        
        # Racing stripes
        draw.rectangle([7, 1, 8, 14], fill=colors['accent'])
        draw.rectangle([6, 3, 9, 12], fill=colors['accent'])
        
        # Carbon fiber accents
        draw.rectangle([2, 12, 13, 13], fill=colors['secondary'])
        draw.rectangle([1, 6, 2, 8], fill=colors['secondary'])
        draw.rectangle([13, 6, 14, 8], fill=colors['secondary'])
        
        # Classic wheels
        draw.rectangle([0, 4, 2, 6], fill=colors['dark'])
        draw.rectangle([13, 4, 15, 6], fill=colors['dark'])
        draw.rectangle([0, 9, 2, 11], fill=colors['dark'])
        draw.rectangle([13, 9, 15, 11], fill=colors['dark'])
        
        # Vintage headlights
        draw.rectangle([3, 2, 5, 3], fill=colors['accent'])
        draw.rectangle([10, 2, 12, 3], fill=colors['accent'])
        
        return img
    
    def create_divo_texture():
        """Aggressive track-focused design"""
        img = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
        draw = ImageDraw.Draw(img)
        colors = BUGATTI_MODELS['divo']['colors']
        
        # Aggressive angular body
        draw.rectangle([2, 4, 13, 11], fill=colors['primary'])
        draw.rectangle([3, 3, 12, 4], fill=colors['window'])
        
        # Orange aerodynamic accents
        draw.rectangle([1, 5, 2, 10], fill=colors['secondary'])
        draw.rectangle([13, 5, 14, 10], fill=colors['secondary'])
        draw.rectangle([4, 11, 11, 12], fill=colors['secondary'])
        
        # Yellow racing details
        draw.rectangle([5, 1, 10, 2], fill=colors['accent'])
        draw.rectangle([7, 12, 8, 13], fill=colors['accent'])
        
        # Aggressive splitter/diffuser
        draw.rectangle([3, 2, 12, 3], fill=colors['dark'])
        draw.rectangle([2, 11, 13, 12], fill=colors['dark'])
        
        # Racing wheels
        draw.rectangle([0, 4, 2, 7], fill=colors['dark'])
        draw.rectangle([13, 4, 15, 7], fill=colors['dark'])
        draw.rectangle([0, 8, 2, 11], fill=colors['dark'])
        draw.rectangle([13, 8, 15, 11], fill=colors['dark'])
        
        # Sharp headlights
        draw.point((3, 2), fill=colors['accent'])
        draw.point((12, 2), fill=colors['accent'])
        
        return img
    
    def create_type35_texture():
        """Vintage racing design"""
        img = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
        draw = ImageDraw.Draw(img)
        colors = BUGATTI_MODELS['type35']['colors']
        
        # Classic vintage shape
        draw.rectangle([3, 4, 12, 11], fill=colors['primary'])
        
        # Brass/copper details
        draw.rectangle([2, 5, 3, 10], fill=colors['secondary'])
        draw.rectangle([12, 5, 13, 10], fill=colors['secondary'])
        draw.rectangle([4, 11, 11, 12], fill=colors['secondary'])
        
        # Vintage racing number circle
        draw.rectangle([6, 6, 9, 9], fill=colors['accent'])
        draw.rectangle([7, 7, 8, 8], fill=colors['primary'])
        
        # Exposed mechanical elements
        draw.rectangle([5, 3, 10, 4], fill=colors['dark'])
        draw.rectangle([3, 12, 12, 13], fill=colors['dark'])
        
        # Vintage wire wheels
        draw.rectangle([1, 4, 3, 6], fill=colors['secondary'])
        draw.rectangle([12, 4, 14, 6], fill=colors['secondary'])
        draw.rectangle([1, 9, 3, 11], fill=colors['secondary'])
        draw.rectangle([12, 9, 14, 11], fill=colors['secondary'])
        
        # Simple headlights
        draw.point((4, 3), fill=colors['accent'])
        draw.point((11, 3), fill=colors['accent'])
        
        return img
    
    def create_centodieci_texture():
        """Ultra-luxury pearl white"""
        img = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
        draw = ImageDraw.Draw(img)
        colors = BUGATTI_MODELS['centodieci']['colors']
        
        # Pearl white body
        draw.rectangle([2, 3, 13, 12], fill=colors['primary'])
        draw.rectangle([4, 2, 11, 3], fill=colors['window'])
        
        # Gold luxury accents
        draw.rectangle([1, 5, 2, 6], fill=colors['secondary'])
        draw.rectangle([13, 5, 14, 6], fill=colors['secondary'])
        draw.rectangle([1, 8, 2, 9], fill=colors['secondary'])
        draw.rectangle([13, 8, 14, 9], fill=colors['secondary'])
        draw.rectangle([5, 12, 10, 13], fill=colors['secondary'])
        
        # Diamond/crystal details
        draw.point((6, 4), fill=colors['accent'])
        draw.point((9, 4), fill=colors['accent'])
        draw.point((6, 7), fill=colors['accent'])
        draw.point((9, 7), fill=colors['accent'])
        draw.point((7, 10), fill=colors['accent'])
        draw.point((8, 10), fill=colors['accent'])
        
        # Premium wheels
        draw.rectangle([1, 4, 2, 6], fill=colors['dark'])
        draw.rectangle([13, 4, 14, 6], fill=colors['dark'])
        draw.rectangle([1, 9, 2, 11], fill=colors['dark'])
        draw.rectangle([13, 9, 14, 11], fill=colors['dark'])
        
        # Luxury headlights
        draw.rectangle([3, 2, 4, 3], fill=colors['secondary'])
        draw.rectangle([11, 2, 12, 3], fill=colors['secondary'])
        
        return img
    
    def create_bolide_texture():
        """Extreme racing with flames"""
        img = Image.new('RGBA', (16, 16), (0, 0, 0, 0))
        draw = ImageDraw.Draw(img)
        colors = BUGATTI_MODELS['bolide']['colors']
        
        # Matte black base
        draw.rectangle([2, 4, 13, 11], fill=colors['primary'])
        draw.rectangle([3, 3, 12, 4], fill=colors['window'])
        
        # Extreme aerodynamics
        draw.rectangle([0, 5, 2, 10], fill=colors['primary'])
        draw.rectangle([13, 5, 15, 10], fill=colors['primary'])
        draw.rectangle([2, 2, 13, 3], fill=colors['primary'])
        draw.rectangle([1, 11, 14, 13], fill=colors['primary'])
        
        # Neon cyan accents
        draw.rectangle([7, 1, 8, 14], fill=colors['secondary'])
        draw.rectangle([1, 7, 14, 8], fill=colors['secondary'])
        
        # Orange flame details
        draw.point((5, 3), fill=colors['accent'])
        draw.point((10, 3), fill=colors['accent'])
        draw.point((4, 9), fill=colors['accent'])
        draw.point((11, 9), fill=colors['accent'])
        draw.rectangle([6, 12, 9, 13], fill=colors['accent'])
        
        # Racing wheels
        draw.rectangle([0, 4, 2, 7], fill=colors['dark'])
        draw.rectangle([13, 4, 15, 7], fill=colors['dark'])
        draw.rectangle([0, 8, 2, 11], fill=colors['dark'])
        draw.rectangle([13, 8, 15, 11], fill=colors['dark'])
        
        # Aggressive LED headlights
        draw.rectangle([3, 2, 12, 3], fill=colors['secondary'])
        
        return img
    
    def create_model_color_file(model_key):
        """Create color specification file for each model"""
        colors = BUGATTI_MODELS[model_key]['colors']
        name = BUGATTI_MODELS[model_key]['name']
        style = BUGATTI_MODELS[model_key]['style']
        
        content = f"""# {name} Color Specification
# Style: {style}

Primary Color: rgb{colors['primary'][:3]} (#{colors['primary'][0]:02x}{colors['primary'][1]:02x}{colors['primary'][2]:02x})
Secondary Color: rgb{colors['secondary'][:3]} (#{colors['secondary'][0]:02x}{colors['secondary'][1]:02x}{colors['secondary'][2]:02x})
Accent Color: rgb{colors['accent'][:3]} (#{colors['accent'][0]:02x}{colors['accent'][1]:02x}{colors['accent'][2]:02x})
Dark Details: rgb{colors['dark'][:3]} (#{colors['dark'][0]:02x}{colors['dark'][1]:02x}{colors['dark'][2]:02x})
Window Tint: rgb{colors['window'][:3]} (#{colors['window'][0]:02x}{colors['window'][1]:02x}{colors['window'][2]:02x})

Design Elements:
"""
        
        if style == 'elegant':
            content += "- Chrome accents and horseshoe grille\n- Premium wheel design\n- Balanced proportions"
        elif style == 'classic_racing':
            content += "- Racing stripes\n- Carbon fiber elements\n- Vintage-inspired headlights"
        elif style == 'aggressive':
            content += "- Angular aerodynamic elements\n- Bold color accents\n- Track-focused design"
        elif style == 'vintage':
            content += "- Exposed mechanical parts\n- Racing number circle\n- Wire wheel details"
        elif style == 'luxury':
            content += "- Pearl finish effect\n- Gold luxury accents\n- Crystal-inspired details"
        elif style == 'racing_extreme':
            content += "- Extreme aerodynamics\n- Neon accent lighting\n- Flame detail elements"
        
        return content
    
    # Create texture functions mapping
    texture_functions = {
        'chiron': create_chiron_texture,
        'veyron': create_veyron_texture,
        'divo': create_divo_texture,
        'type35': create_type35_texture,
        'centodieci': create_centodieci_texture,
        'bolide': create_bolide_texture
    }
    
    # Create directories
    os.makedirs('src/main/resources/assets/yourmod/textures/item', exist_ok=True)
    os.makedirs('src/main/resources/assets/yourmod/textures/entity', exist_ok=True)
    
    print("🎨 Creating unique Bugatti model textures...\n")
    
    # Generate textures for each model
    for model_key, create_func in texture_functions.items():
        model_name = BUGATTI_MODELS[model_key]['name']
        
        # Create item texture
        texture = create_func()
        filename = f'bugatti_{model_key}.png'
        filepath = f'src/main/resources/assets/yourmod/textures/item/{filename}'
        texture.save(filepath)
        
        # Create color specification file
        color_content = create_model_color_file(model_key)
        color_filepath = f'src/main/resources/assets/yourmod/textures/item/bugatti_{model_key}_colors.txt'
        with open(color_filepath, 'w') as f:
            f.write(color_content)
        
        print(f"✓ Created {model_name}")
        print(f"  - Texture: {filename}")
        print(f"  - Colors: bugatti_{model_key}_colors.txt")
        print(f"  - Style: {BUGATTI_MODELS[model_key]['style']}")
        print()
    
    # Create summary file
    summary_content = """# Bugatti Model Textures Summary

This directory contains unique textures for each Bugatti model:

## Models and Their Characteristics:

1. **Bugatti Chiron** - Flagship elegance
   - Classic Bugatti blue with silver chrome
   - Balanced, premium design
   - Signature horseshoe grille

2. **Bugatti Veyron** - Classic racing heritage  
   - Deep blue with carbon fiber
   - White racing stripes
   - Vintage-inspired elements

3. **Bugatti Divo** - Track-focused aggression
   - Matte dark blue base
   - Orange/yellow racing accents
   - Angular aerodynamic design

4. **Bugatti Type 35** - Vintage racing legend
   - Classic racing blue
   - Brass/copper vintage details
   - Racing number circle

5. **Bugatti Centodieci** - Ultra-luxury exclusivity
   - Pearl white finish
   - Gold luxury accents
   - Crystal-inspired details

6. **Bugatti Bolide** - Extreme racing machine
   - Matte black base
   - Neon cyan accents
   - Orange flame details

Each model has its own texture file and color specification for easy identification and modification.
"""
    
    with open('src/main/resources/assets/yourmod/textures/item/BUGATTI_MODELS_README.txt', 'w') as f:
        f.write(summary_content)
    
    print("🏁 All unique Bugatti textures created successfully!")
    print("📋 Summary file: BUGATTI_MODELS_README.txt")
    print("\n🔧 Build the mod with: ./gradlew build")
    print("🎮 Test textures in-game to verify designs")
    
except ImportError:
    print("❌ PIL (Pillow) not installed. Install it with:")
    print("pip3 install Pillow")
    print("\nColors for manual creation:")
    for model, data in BUGATTI_MODELS.items():
        print(f"\n{data['name']}:")
        for color_name, rgba in data['colors'].items():
            hex_color = f"#{rgba[0]:02x}{rgba[1]:02x}{rgba[2]:02x}"
            print(f"  {color_name}: {hex_color}")
