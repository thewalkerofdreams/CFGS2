﻿#pragma checksum "C:\Users\thewa\Desktop\SpaceInvaders\SpaceInvaders\SpaceInvaders_UI\Views\MainPage.xaml" "{406ea660-64cf-4c82-b6f0-42d48172a799}" "36052A909659E0757DAAAE43A034EC44"
//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated by a tool.
//
//     Changes to this file may cause incorrect behavior and will be lost if
//     the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace SpaceInvaders_UI
{
    partial class MainPage : 
        global::Windows.UI.Xaml.Controls.Page, 
        global::Windows.UI.Xaml.Markup.IComponentConnector,
        global::Windows.UI.Xaml.Markup.IComponentConnector2
    {
        /// <summary>
        /// Connect()
        /// </summary>
        [global::System.CodeDom.Compiler.GeneratedCodeAttribute("Microsoft.Windows.UI.Xaml.Build.Tasks"," 10.0.18362.1")]
        [global::System.Diagnostics.DebuggerNonUserCodeAttribute()]
        public void Connect(int connectionId, object target)
        {
            switch(connectionId)
            {
            case 2: // Views\MainPage.xaml line 16
                {
                    global::Windows.UI.Xaml.Controls.RelativePanel element2 = (global::Windows.UI.Xaml.Controls.RelativePanel)(target);
                    ((global::Windows.UI.Xaml.Controls.RelativePanel)element2).Loaded += this.allowfocus_Loaded;
                }
                break;
            case 3: // Views\MainPage.xaml line 17
                {
                    this.StackPanel01 = (global::Windows.UI.Xaml.Controls.StackPanel)(target);
                }
                break;
            case 4: // Views\MainPage.xaml line 23
                {
                    this.Canvas01 = (global::Windows.UI.Xaml.Controls.Canvas)(target);
                }
                break;
            case 5: // Views\MainPage.xaml line 24
                {
                    this.nave = (global::Windows.UI.Xaml.Controls.Image)(target);
                }
                break;
            case 6: // Views\MainPage.xaml line 19
                {
                    this.VidasTotales = (global::Windows.UI.Xaml.Controls.TextBlock)(target);
                }
                break;
            case 7: // Views\MainPage.xaml line 21
                {
                    this.AsteroidesDestruidos = (global::Windows.UI.Xaml.Controls.TextBlock)(target);
                }
                break;
            default:
                break;
            }
            this._contentLoaded = true;
        }

        /// <summary>
        /// GetBindingConnector(int connectionId, object target)
        /// </summary>
        [global::System.CodeDom.Compiler.GeneratedCodeAttribute("Microsoft.Windows.UI.Xaml.Build.Tasks"," 10.0.18362.1")]
        [global::System.Diagnostics.DebuggerNonUserCodeAttribute()]
        public global::Windows.UI.Xaml.Markup.IComponentConnector GetBindingConnector(int connectionId, object target)
        {
            global::Windows.UI.Xaml.Markup.IComponentConnector returnValue = null;
            return returnValue;
        }
    }
}
