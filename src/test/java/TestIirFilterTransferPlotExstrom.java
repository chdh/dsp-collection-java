// Copyright 2013 Christian d'Heureuse, Inventec Informatik AG, Zurich, Switzerland
// www.source-code.biz, www.inventec.ch/chdh
//
// This module is multi-licensed and may be used under the terms
// of any of the following licenses:
//
//  EPL, Eclipse Public License, V1.0 or later, http://www.eclipse.org/legal
//  LGPL, GNU Lesser General Public License, V2.1 or later, http://www.gnu.org/licenses/lgpl.html
//
// Please contact the author if you need another license.
// This module is provided "as is", without warranties of any kind.

import biz.source_code.dsp.filter.FilterPassType;
import biz.source_code.dsp.filter.IirFilterCoefficients;
import biz.source_code.dsp.filter.IirFilterDesignExstrom;

// Plots the transfer curve of an IIR filter designed with the Exstrom method.
public class TestIirFilterTransferPlotExstrom {

public static void main (String[] args) throws Exception {
   if (args.length != 4) {
      throw new Exception("Invalid number of command line arguments."); }
   FilterPassType filterPassType = FilterPassType.valueOf(args[0]);
   int filterOrder = Integer.valueOf(args[1]);
   double fcf1 = Double.valueOf(args[2]);
   double fcf2 = Double.valueOf(args[3]);
   IirFilterCoefficients coeffs = IirFilterDesignExstrom.design(filterPassType, filterOrder, fcf1, fcf2);
   TestIirFilterTransferPlot.start(coeffs); }

}
