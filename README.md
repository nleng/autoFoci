# AutoFoci
Automated foci counting software especially designed for a high-throughput object detection in single cell images. Analysed images were obtained by immunofluorescence microscopy using a double labelling approach with two well established DNA double-strand break (DSB) markers, γH2AX and 53BP1 to visualize DSBs as so-called foci after the exposure of cells to low radiation doses. For a detailed description please see [N. Lengert et al.](https://www.nature.com/articles/s41598-018-35660-5)

This repository contains all files needed to perform both, the image processing to generate  single cell images as well as the subsequent counting of foci with AutoFoci. You can download the complete repository as a zip file via [this link](https://github.com/nleng/AutoFoci/archive/master.zip). 

## 1. Generation of single cell images

The ImageJ version including the [cellect tools](ImageJ/Cellect_Installation.7z?raw=true) for image processing and [test images](ImageJ/Test_Images_ImageJ.7z?raw=true) (both provided as zip file) needed to try  this step out, are included in the ImageJ folder. Images created by this step are ready to be analysed in AutoFoci. Please follow the instructions in the [PDF guide](ImageJ/Guidance_to_process_images_using_Cellect.pdf).

The ImageJ version provided was tested on Windows 7 operating system. Typical installation time is only a few minutes. The expected run time for the demo data is less than 10 minutes to generate about 400 single cell images depending on population selection as explained in step 5 of the [PDF guide](ImageJ/Guidance_to_process_images_using_Cellect.pdf).



## 2. Foci counting with AutoFoci

The program AutoFoci (provided as [AutoFoci.jar](AutoFoci/AutoFoci.jar?raw=true) file) and [test images](AutoFoci/Test_Images_AutoFoci.7z?raw=true) (provided as zip file) needed to test foci counting with AutoFoci are included in the AutoFoci folder. Please follow the instructions in the [PDF guide](AutoFoci/Guidance_to_count_foci_using_AutoFoci.pdf). To use AutoFoci, please make sure that [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) is installed on your operating system. The ImageJ version used within AutoFoci is not compatible with Java 9, but we are working on support for it. AutoFoci was tested on Windows 7 as well as Ubuntu 16.04. Typical installation time is only a few minutes. 

The expected run time for the demo data is less than 10 minutes. The estimated number of foci per cell by automatic threshold methods should be around 0.31 with a similar value after manual validation as explained in step 4 and 5 of the [PDF guide](AutoFoci/Guidance_to_count_foci_using_AutoFoci.pdf). 

Additionally, the file [manual_object_rating.7z](manual_object_rating.7z?raw=true) contains an image stack with marked objects as well as an Excel sheet with the corresponding manual rating by three experimenters, which can be used as a starting point for experimenters without much experience in counting foci.

