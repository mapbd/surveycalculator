/*
 * This file is part of Compass.
 * Copyright (C) 2024 Philipp Bobek <philipp.bobek@mailbox.org>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Compass is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
//    id ("com.android.application") version "8.6.1" apply false
    id ("com.android.library") version "7.3.1" apply false
//    id ("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false
}

buildscript {

    repositories {
        google()  // maven { url("https://maven.google.com") } for Gradle <= 3
        mavenCentral()
    }
    dependencies {
        classpath("com.google.android.gms:oss-licenses-plugin:0.10.6")

        val navVersion = "2.7.5"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
    }
}