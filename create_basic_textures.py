#!/usr/bin/env python3
"""
Create very basic solid color PNG textures without any dependencies
Uses only built-in Python capabilities
"""

import struct
import zlib
import os

def create_png(width, height, r, g, b, a=255):
    """Create a simple solid color PNG"""
    
    # PNG signature
    png_signature = b'\x89PNG\r\n\x1a\n'
    
    # Create RGBA pixel data
    pixel_data = bytearray()
    for y in range(height):
        pixel_data.append(0)  # Filter type (0 = None)
        for x in range(width):
            pixel_data.extend([r, g, b, a])  # RGBA
    
    # Compress pixel data
    compressed_data = zlib.compress(bytes(pixel_data))
    
    # IHDR chunk
    ihdr_data = struct.pack('>IIBBBBB', width, height, 8, 6, 0, 0, 0)  # 8-bit RGBA
    ihdr_crc = zlib.crc32(b'IHDR' + ihdr_data) & 0xffffffff
    ihdr_chunk = struct.pack('>I', len(ihdr_data)) + b'IHDR' + ihdr_data + struct.pack('>I', ihdr_crc)
    
    # IDAT chunk
    idat_crc = zlib.crc32(b'IDAT' + compressed_data) & 0xffffffff
    idat_chunk = struct.pack('>I', len(compressed_data)) + b'IDAT' + compressed_data + struct.pack('>I', idat_crc)
    
    # IEND chunk
    iend_crc = zlib.crc32(b'IEND') & 0xffffffff
    iend_chunk = struct.pack('>I', 0) + b'IEND' + struct.pack('>I', iend_crc)
    
    return png_signature + ihdr_chunk + idat_chunk + iend_chunk

# Create directories
os.makedirs('src/main/resources/assets/yourmod/textures/item', exist_ok=True)
os.makedirs('src/main/resources/assets/yourmod/textures/entity', exist_ok=True)

print("🎨 Creating Bugati car textures...")

# Bugati car colors (RGB values)
cars = {
    'bugatti_car': (43, 108, 196),      # Bugati Blue
    'bugatti_veyron': (26, 26, 26),     # Dark/Black  
    'bugatti_divo': (255, 69, 0),       # Orange Red
    'bugatti_type35': (65, 105, 225),   # Royal Blue
    'bugatti_centodieci': (255, 255, 255), # White
    'bugatti_bolide': (0, 0, 0),        # Black
}

# Create 16x16 item textures
for car_name, (r, g, b) in cars.items():
    png_data = create_png(16, 16, r, g, b)
    with open(f'src/main/resources/assets/yourmod/textures/item/{car_name}.png', 'wb') as f:
        f.write(png_data)
    print(f"✓ Created {car_name} texture (RGB: {r},{g},{b})")

# Create 128x64 entity texture for main car
main_entity_texture = create_png(128, 64, 43, 108, 196)  # Bugati Blue
with open('src/main/resources/assets/yourmod/textures/entity/bugatti_car.png', 'wb') as f:
    f.write(main_entity_texture)
print("✓ Created entity texture for Bugatti Chiron")

# Create car parts textures
parts = {
    'car_engine': (96, 96, 96),     # Gray
    'car_wheel': (32, 32, 32),      # Dark Gray
    'car_chassis': (128, 128, 128), # Light Gray
}

for part_name, (r, g, b) in parts.items():
    png_data = create_png(16, 16, r, g, b)
    with open(f'src/main/resources/assets/yourmod/textures/item/{part_name}.png', 'wb') as f:
        f.write(png_data)
    print(f"✓ Created {part_name} texture")

print(f"\n🎉 All textures created successfully!")
print(f"Your Bugati cars will now show their proper colors:")
print(f"- Chiron: Bugati Blue")
print(f"- Veyron: Black")  
print(f"- Divo: Orange")
print(f"- Type 35: Royal Blue")
print(f"- Centodieci: White")
print(f"- Bolide: Black")
