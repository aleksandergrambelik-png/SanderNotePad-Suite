# Sander Suite Notepad v2.0

Standalone OSINT and Security Suite for Android. Built entirely in pure Java with zero external dependencies, no Gradle, and no root access required.

---

## Download

Latest Release: [Sander Suite Notepad v2.0.max.null](https://github.com/aleksandergrambelik-png/SanderSuiteNotpad/releases/tag/v2.0max.null)

- APK Size: 608 KB
- SHA-256: 8ef83e427e2763a1a5eee05606e41d49f85a8830dbc32d2ac1f053267c65b83a

---

## POC

<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/f86f0fe9-a100-4cc1-a537-695281685642" width="150" /></td>
    <td><img src="https://github.com/user-attachments/assets/f51c93cd-b793-425b-b827-051368c9007d" width="150" /></td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/aae6820c-68dc-4861-a68a-b43def8679bc" width="150" /></td>
    <td><img src="https://github.com/user-attachments/assets/cf6579e5-d391-48fa-a4ea-acc46f890dc1" width="150" /></td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/4e90c877-7057-41dd-a34c-023716c772f6" width="150" /></td>
    <td><img src="https://github.com/user-attachments/assets/e5eca153-a310-41a6-b15d-b7bc8a0b506a" width="150" /></td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/3b256f9a-92ec-4c4b-b495-693252cf96c8" width="150" /></td>
    <td><img src="https://github.com/user-attachments/assets/4bc0e14e-5509-433f-affa-44af1c30998d" width="150" /></td>
  </tr>
</table>

## Features and Usage

### OSINT Toolkit
- Email Recon: Check if an email has been exposed in a data breach (via XposedOrNot).
- DNS Lookup: Retrieve MX records for a domain (via Google DNS).
- Whois Lookup: Get domain registration details (via whois.vu).
- IP Geolocation: Find the country, city, and ISP of an IP address (via ip-api.com).
- Phone Info: Detect the mobile operator based on local Indonesian prefixes (100% offline).

### Search and Recon
- Wikipedia Search: Fetch full article summaries in Indonesian or English.
- Subdomain Finder: Discover subdomains using rotating APIs (crt.sh + HackerTarget).
- GitHub Search: Find repositories and users on GitHub without an API key.

### Defense and Security
- PIN Lock: 4-digit PIN encrypted with AES-256 via Android Keystore (TEE). No plain-text PINs stored.
- Anti-Tamper: SHA-256 signature check. If the APK is repacked or modified, it will refuse to run.
- Safety Info: Displays the creator information, license, and APK integrity status.

### Utilities
- Notepad: A minimalist text editor with internal storage save/load.
- Mini Game: A fully playable Tetris clone built with SurfaceView and Canvas.
- Bilingual UI: Toggle between Indonesian (ID) and English (EN).

---

## Installation

1. Download the latest SanderNotepad.apk from the Releases tab.
2. Install it on your Android device (API 26+).
3. Set up your 4-digit PIN on first launch.
4. Start exploring.

---

## Source Code Notice

This repository is a partial source release.

The UI Layer (MainActivity, SanderUI, SanderScreen) and Resources (res/) are public.

The Core Logic Modules (api/, core/, osint/, security/, game/) are kept private. This is to prevent unauthorized tampering, to protect the OSINT API endpoints, and to ensure the integrity of the PIN Lock system.

If you want to use the app, please download the compiled APK from the Releases tab.

---

## License

This project is licensed under the GNU Affero General Public License v3.0 (AGPL-3.0).
See the LICENSE file for details.

---

## Credits and AI Collaboration

- Idea, Architecture and Core Logic: Aleksander Titan Pabiyuri (@aleksandergrambelik-png)
- AI Assistance (Debugging): DeepSeek V4.1 Pro (and other LLMs used during development)

The tools used here are merely instruments. The final design, quality control, and decision-making remain in the hands of the developer.

---
Copyright (C) 2026 Aleksander Titan Pabiyuri.
