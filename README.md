# Monster Hunter Rating Site Admin

This is the administrative backend for a Monster Hunter rating site. It owns the actual monster data — name, species, elemental weakness, threat rank, and the aggregate rating each monster has picked up — and gives whoever runs the site a way to add, edit, and remove that data without touching a database directly.

## How it fits into the larger system

The plan is to split the rating site into two halves that can grow on their own:

- **The admin app (this repo)** is where the monster catalog lives and gets maintained. It's the source of truth for what monsters exist and what data is attached to each one.
- **A public-facing site** (not built yet) would be the part hunters actually use — browsing monsters, checking weaknesses before a hunt, and leaving their own ratings. It would read from the same catalog this admin app maintains, and eventually feed back the rating totals this app currently seeds by hand.

Keeping the two apart means the admin side can stay simple and internal — no public traffic, no accounts, no abuse to worry about — while the public site can be built (or rebuilt) later without having to touch how the underlying data is managed.

## Features

- Browse the full monster catalog in a sortable table
- Add a new monster with name, species, weakness, threat rank, and rating info
- Edit or delete an existing entry
- View the full details for a single monster

## Tech stack

- **Framework:** ASP.NET Core MVC (.NET 10)
- **Language:** C# / Razor views
- **Data access:** Entity Framework Core with SQLite
- **Styling:** Bootstrap 5
- **Client-side:** jQuery, jQuery Validation

## Getting started

**Prerequisites:** [.NET 10 SDK](https://dotnet.microsoft.com/download), or [Visual Studio](https://visualstudio.microsoft.com/) if you'd rather not use the command line.

**Run it from the command line:**

```bash
git clone https://github.com/LucasCowie/MonsterHunterRatingSiteAdmin.git
cd MonsterHunterRatingSiteAdmin/MonsterHunterRatingSiteAdmin
dotnet restore
dotnet run
```

The app starts on `https://localhost:7201` (and `http://localhost:5251`) and creates a local `monsters.db` SQLite file the first time it runs, seeded with a couple of sample monsters.

**Or from Visual Studio:** open `MonsterHunterRatingSiteAdmin.slnx` and press F5.

## Project structure

```
MonsterHunterRatingSiteAdmin/
├── Controllers/     # HomeController + MonstersController (the CRUD logic)
├── Models/          # Monster entity + ErrorViewModel
├── Data/            # MonstersDbContext (EF Core)
├── Views/           # Razor views for Home and Monsters
├── wwwroot/         # Static assets (css, js, Bootstrap/jQuery libs)
└── Program.cs        # App startup & middleware pipeline
```

## Roadmap

- [ ] Images for each monster
- [ ] The actual public-facing rating site that reads from this catalog
- [ ] Search and filtering by species/weakness
- [ ] User accounts, so ratings come from real hunters instead of a seeded average

## Resources

- [ASP.NET Core MVC docs](https://learn.microsoft.com/aspnet/core/mvc/overview)
- [EF Core docs](https://learn.microsoft.com/ef/core/)
- [Bootstrap docs](https://getbootstrap.com/)
