# ReLiS

*ReLiS is a tool for conducting systematic reviews.*

Systematic review is a technique used to search for evidence in scientific literature that is conducted in a formal manner, following a well-defined process, according to a previously elaborated protocol. Conducting a systematic reviews involves many steps over a long period of time, and is often laborious and repetitive. This is why we have created ReLiS which provides essential software support to reviewers in conducting high quality systematic reviews. With ReLiS, you can plan, conduct, and report your review. 

Unlike other systematic review tools, ReLiS is an online tool that automatically installs and configures your projects. You conduct reviews collaboratively and iteratively on the cloud. ReLiS is engineered following a model-driven development approach. It features a domain-specific modeling editor and an architecture that enables on-the-fly installation and (re)configuration of multiple concurrently running projects.

You can use a publically available instance of ReLiS at [http://relis.iro.umontreal.ca/](http://relis.iro.umontreal.ca/). This GitHub repository allows you to install ReLiS on your servers.

# ReLiS automated tests

this projec implements the automated tests of ReLiS **develop branch**.

# Installation
##
You can run localy all the tests, in order to do so, follow to next steps

# for Linux

- after the git clone, you can this run the file 'build_test.sh'

# for Windows

- you can follows the steps of [https://github.com/geodes-sms/relis/tree/develop#relis](https://github.com/geodes-sms/relis/tree/develop#relis). to install ReLiS and run ReLiS on your machine.
- you can go the root folder of this project, and your terminal run **mvn clean test**.


# Test of choice to run

You can choose the tests that you wanna tests, the file **'relis_test.xml'** contains all the tests and the project to choose for the tests,
you can comment a test if you wanna ignore it.
if you wanna to use a headless chrome web driver you can go to **Utils folder** and then open the file **Initializer.java** at the function **chromeDriver** to uncomment the line
**ChromeOptions.addArguments("--headless", "window-size=1024,768", "--no-sandbox");**.





