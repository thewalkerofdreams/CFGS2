namespace CRUDXamarin.viewModels
{
    using System;
    using System.IO;



    public class ConversionImagenes
    {

    //    /// <summary>
    //    /// Metodo que convierte un Storagefile en array de bytes
    //    /// </summary>
    //    /// <param name="file">
    //    /// StorageFile archivo
    //    /// </param>
    //    /// <returns>
    //    /// array de bytes
    //    /// </returns>
    //    public async Task<byte[]> AsByteArray(StorageFile file)
    //    {
    //        IRandomAccessStream fileStream = await file.OpenAsync(FileAccessMode.Read);
    //        var reader = new Windows.Storage.Streams.DataReader(fileStream.GetInputStreamAt(0));
    //        await reader.LoadAsync((uint)fileStream.Size);

    //        byte[] pixels = new byte[fileStream.Size];

    //        reader.ReadBytes(pixels);

    //        return pixels;
    //    }

    ///// <summary>
    ///// Convierte array de bytes en bitmap image
    ///// </summary>
    ///// <param name="byteArray">
    ///// array de bytes
    ///// </param>
    ///// <returns>
    ///// bitmapimage 
    ///// </returns>
    //    public BitmapImage AsBitmapImage(byte[] byteArray)
    //    {
    //        if (byteArray != null)
    //        {
    //            using (var stream = new InMemoryRandomAccessStream())
    //            {
    //                stream.WriteAsync(byteArray.AsBuffer()).GetResults(); 
    //                var image = new BitmapImage();
    //                stream.Seek(0);
    //                image.SetSource(stream);
    //                return image;
    //            }
    //        }

    //        return null;
    //    }

    //    /// <summary>
    //    /// Convierte de StorageFile a bitmapimage
    //    /// </summary>
    //    /// <param name="file"> storage file </param>
    //    /// <returns>bitmapimage </returns>
    //    private async Task<BitmapImage> AsBitmapImage(StorageFile file)
    //    {
    //        var stream = await file.OpenAsync(FileAccessMode.Read);
    //        var bitmapImage = new BitmapImage();
    //        await bitmapImage.SetSourceAsync(stream);
    //        return bitmapImage;
    //    }
    }
}