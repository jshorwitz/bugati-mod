# Contributing to Bugati Cars Mod

Thank you for your interest in contributing to the Bugati Cars Mod! 🏎️

## Getting Started

1. **Fork** the repository on GitHub
2. **Clone** your fork locally: `git clone https://github.com/YOUR_USERNAME/bugati-mod.git`
3. **Create** a new branch: `git checkout -b feature/your-feature-name`

## Development Setup

1. **Prerequisites**: Java 17 or higher
2. **Build**: `./gradlew build`
3. **Test**: `./gradlew runClient`

## How to Contribute

### 🚗 Adding New Car Models
- Follow the existing pattern in `src/main/java/com/yourname/yourmod/entity/`
- Create entity class extending `Animal`
- Add item class for spawning
- Register in `ModEntityTypes` and `ModItems`
- Add language translations

### 🎨 Improving Visuals
- Textures go in `src/main/resources/assets/yourmod/textures/`
- Models go in `src/main/resources/assets/yourmod/models/`
- Follow 16x16 pixel format for item textures

### 🔧 Bug Fixes
- Check existing issues first
- Create detailed bug reports with steps to reproduce
- Test your fixes thoroughly

### 📚 Documentation
- Update README.md for new features
- Add code comments for complex logic
- Update version info in relevant files

## Code Style

- Follow existing Java conventions
- Use meaningful variable and method names
- Add comments for complex algorithms
- Keep methods reasonably sized

## Pull Request Process

1. **Test** your changes thoroughly
2. **Update** documentation if needed
3. **Create** a detailed pull request description
4. **Reference** any related issues
5. **Be patient** during the review process

## Car Performance Guidelines

When adding new car models, consider these performance tiers:

| Tier | Speed Range | Fuel Capacity | Special Features |
|------|-------------|---------------|------------------|
| **Vintage** | 1.0-1.5 blocks/s | 400-600 units | Classic sounds, wood fuel |
| **Modern** | 1.5-2.0 blocks/s | 800-1200 units | Balanced performance |
| **Hypercar** | 2.0-2.5 blocks/s | 600-800 units | Special effects, premium fuel |

## Questions?

- Check existing [Issues](https://github.com/maxwellhorwitz/bugati-mod/issues)
- Start a [Discussion](https://github.com/maxwellhorwitz/bugati-mod/discussions)
- Contact [@maxwellhorwitz](https://github.com/maxwellhorwitz)

Thanks for contributing to the automotive excellence in Minecraft! 🚀
