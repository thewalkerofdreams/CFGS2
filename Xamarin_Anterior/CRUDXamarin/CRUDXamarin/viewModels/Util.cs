using System;
using System.Collections.Generic;
using System.Linq;
using System.Drawing;
using System.Text;
using System.Threading.Tasks;

using System.IO;


namespace CRUDXamarin.viewModels
{

    public class Util
    {
        
        ///*Convertir array de bytes to bitmap*/
        //public async Task<BitmapImage> arrayBytesABitmapImage(Byte[] imagenEnBytes)
        //{
        //    using (InMemoryRandomAccessStream stream = new InMemoryRandomAccessStream())
        //    {
        //        using (DataWriter writer = new DataWriter(stream.GetOutputStreamAt(0)))
        //        {
        //            writer.WriteBytes(imagenEnBytes);
        //            await writer.StoreAsync();
        //        }

        //        var image = new BitmapImage();
        //        await image.SetSourceAsync(stream);

        //        return image;
        //    }
        //}


        ///*De StorageFile a Bitmap Image*/
        //public async Task<BitmapImage> StorageFileABitmapImage(StorageFile file)
        //{
        //    using (var inputStream = await file.OpenSequentialReadAsync())
        //    {
        //        var readStream = inputStream.AsStreamForRead();

        //        var byteArray = new byte[readStream.Length];
        //        await readStream.ReadAsync(byteArray, 0, byteArray.Length);

                
        //        return await arrayBytesABitmapImage(byteArray); ;
        //    }
        //}

        ///*De bitmapimage a byte[]*/
        //public async Task<byte[]> BitmapImagetoArrayByte(BitmapImage bitmapImage)
        //{
        //    StorageFile file = await StorageFile.GetFileFromApplicationUriAsync(bitmapImage.UriSource);

        //    return await StorageFileToArrayByte(file);
        //}


        ///*Storagefile to byte array*/
        //public static async Task<byte[]> StorageFileToArrayByte(StorageFile file)
        //{
        //    byte[] fileBytes = null;
        //    if (file == null) return null;
        //    using (var stream = await file.OpenReadAsync())
        //    {
        //        fileBytes = new byte[stream.Size];
        //        using (var reader = new DataReader(stream))
        //        {
        //            await reader.LoadAsync((uint)stream.Size);
        //            reader.ReadBytes(fileBytes);
        //        }
        //    }
        //    return fileBytes;
        //}
    }
}
